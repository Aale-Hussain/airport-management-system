var map = new ol.Map({
    target: 'map', //which div to put map
    layers: [
        new ol.layer.Tile({
            source: new ol.source.OSM() //download the image from openstreet map and visualizze
        })
    ],
    view: new ol.View({
        center: ol.proj.fromLonLat([8.6081 , 45.4342 ]), // fauser
        zoom: 6
    })
});
var tagMap = {
    'airport':     ['aeroway',  'aerodrome']
};
var markerSource = new ol.source.Vector();
var markerLayer  = new ol.layer.Vector({ source: markerSource });
map.addLayer(markerLayer);

function searchPOI() {
    var city = document.getElementById('city').value.trim();
    const type = "airport";

    if (!city || !type) {
        alert('Inserisci città e tipo');
        return;
    }

    var tag = tagMap[type];
    if (!tag) {
        alert('Tipo non supportato. Usa: ' + Object.keys(tagMap).join(', '));
        return;
    }

    document.getElementById('result-count').textContent = 'Ricerca in corso...';

    var nominatimUrl = 'https://nominatim.openstreetmap.org/search'
        + '?q=' + encodeURIComponent(city)
        + '&format=json&limit=1';

    fetch(nominatimUrl, {
        headers: { 'Accept-Language': 'it' }
    })
        .then(function(res) { return res.json(); })
        .then(function(data) {
            if (data.length === 0) {
                alert('Città non trovata');
                document.getElementById('result-count').textContent = '';
                return;
            }

            var place = data[0];
            var bb    = place.boundingbox;
            var margin = 0.05;
            var south = parseFloat(bb[0]) - margin;
            var north = parseFloat(bb[1]) + margin;
            var west  = parseFloat(bb[2]) - margin;
            var east  = parseFloat(bb[3]) + margin;

            map.getView().animate({
                center:   ol.proj.fromLonLat([parseFloat(place.lon), parseFloat(place.lat)]),
                zoom:     12,
                duration: 800
            });

            queryOverpass(south, west, north, east, tag);
        })
        .catch(function(err) {
            console.error('Nominatim error:', err);
            alert('Errore geocoding');
        });
}

function queryOverpass(south, west, north, east, tag) {
    var query = '[out:json][timeout:25];('
        + 'node["' + tag[0] + '"="' + tag[1] + '"](' + south + ',' + west + ',' + north + ',' + east + ');'
        + 'way["'  + tag[0] + '"="' + tag[1] + '"](' + south + ',' + west + ',' + north + ',' + east + ');'
        + 'relation["' + tag[0] + '"="' + tag[1] + '"](' + south + ',' + west + ',' + north + ',' + east + ');'
        + ');out center;';
    var url = 'https://overpass-api.de/api/interpreter?data=' + encodeURIComponent(query);

    fetch(url)
        .then(function(res) { return res.json(); })
        .then(function(data) {
            showMarkers(data.elements);
        })
        .catch(function(err) {
            console.error('Overpass error:', err);
            alert('Errore Overpass API');
        });
}

function showMarkers(elements) {
    markerSource.clear();

    elements.forEach(function(node) {
        var lat = node.lat !== undefined ? node.lat : (node.center ? node.center.lat : null);
        var lon = node.lon !== undefined ? node.lon : (node.center ? node.center.lon : null);
        if (lat === null || lon === null) return;

        var feature = new ol.Feature({
            geometry: new ol.geom.Point(ol.proj.fromLonLat([lon, lat])),
            name:     node.tags && node.tags.name ? node.tags.name : 'Senza nome',
            tags:     node.tags || {},
            osm_id:   node.id
        });

        feature.setStyle(new ol.style.Style({
            image: new ol.style.Circle({
                radius: 7,
                fill:   new ol.style.Fill({ color: '#f5a623' }),
                stroke: new ol.style.Stroke({ color: '#0a0e17', width: 2 })
            })
        }));

        markerSource.addFeature(feature);
    });

    document.getElementById('result-count').textContent =
        elements.length + ' risultati trovati';
}

map.on('click', function(evt) {
    var feature = map.forEachFeatureAtPixel(evt.pixel, function(f) { return f; });

    if (feature) {
        document.getElementById('poi-name').textContent    = feature.get('name');
        document.getElementById('poi-address').textContent =
            feature.get('tags')['addr:street'] || '';
        document.getElementById('poi-panel').style.display = 'block';
    } else {
        document.getElementById('poi-panel').style.display = 'none';
    }
});

// button listener — script is at bottom of body so element exists
document.getElementById('search-btn').addEventListener('click', searchPOI);