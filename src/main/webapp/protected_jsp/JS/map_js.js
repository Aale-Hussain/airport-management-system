var map = new ol.Map({
    target: 'map',
    layers: [
        new ol.layer.Tile({
            source: new ol.source.OSM()
        })
    ],
    view: new ol.View({
        center: ol.proj.fromLonLat([8.6081, 45.4342]),
        zoom: 6
    })
});

var tagMap = {
    'airport': ['aeroway', 'aerodrome']
};

var markerSource = new ol.source.Vector();
var markerLayer  = new ol.layer.Vector({ source: markerSource });
map.addLayer(markerLayer);

function searchPOI() {
    var city = document.getElementById('city').value.trim();
    const type = "airport";

    if (!city) {
        alert('Inserisci città');
        return;
    }

    var tag = tagMap[type];
    document.getElementById('result-count').textContent = 'Ricerca in corso...';

    var nominatimUrl = 'https://nominatim.openstreetmap.org/search'
        + '?q=' + encodeURIComponent(city)
        + '&format=json&limit=1';

    fetch(nominatimUrl, { headers: { 'Accept-Language': 'it' } })
        .then(function(res) { return res.json(); })
        .then(function(data) {
            if (data.length === 0) {
                alert('Città non trovata');
                document.getElementById('result-count').textContent = '';
                return;
            }
            var place = data[0];
            var bb     = place.boundingbox;
            var margin = 0.08; //8 km
            var south  = parseFloat(bb[0]) - margin;
            var north  = parseFloat(bb[1]) + margin;
            var west   = parseFloat(bb[2]) - margin;
            var east   = parseFloat(bb[3]) + margin;

            map.getView().animate({
                center: ol.proj.fromLonLat([parseFloat(place.lon), parseFloat(place.lat)]),
                zoom: 12,
                duration: 800
            });

            checkOverpassStatus(function(canProceed) {
                if (canProceed) {
                    queryOverpass(south, west, north, east, tag);
                }
            });
        })


        .catch(function(err) {
            console.error('Nominatim error:', err);
            alert('Errore geocoding');
        });
}

function queryOverpass(south, west, north, east, tag, retryCount) {
    retryCount = retryCount || 0;

    var latSpan = north - south;
    var lonSpan = east  - west;
    if (latSpan > 2 || lonSpan > 2) {
        document.getElementById('result-count').textContent = 'Zoom in più — area troppo grande';
        return;
    }

    // QUERY: includes nodes, ways, AND relations
    var query = '[out:json][timeout:25];('
        + 'nwr["aeroway"="aerodrome"](' + south + ',' + west + ',' + north + ',' + east + ');'
        + ');out center;';

    var servers = [
        'https://overpass-api.de/api/interpreter',
        'https://overpass.kumi.systems/api/interpreter'
    ];
    var serverIndex = retryCount % servers.length;  // alternate servers
    var url = servers[serverIndex] + '?data=' + encodeURIComponent(query);

    document.getElementById('result-count').textContent = 'Caricamento...';

    fetch(url)
        .then(function(res) {
            if (!res.ok) {
                if (retryCount < 3) {  // max 3 retries
                    var waitTime = Math.pow(2, retryCount) * 2000;  // 2s, 4s, 8s
                    document.getElementById('result-count').textContent =
                        'Server occupato, riprovo tra ' + (waitTime/1000) + ' secondi...';

                    setTimeout(function() {
                        queryOverpass(south, west, north, east, tag, retryCount + 1);
                    }, waitTime);
                } else {
                    document.getElementById('result-count').textContent =
                        'Server sovraccarico, riprova più tardi';
                }
                return null;
            }
            return res.json();
        })
        .then(function(data) {
            if (!data) return;
            showMarkers(data.elements);
        })
        .catch(function(err) {
            console.error('Overpass error:', err);
            document.getElementById('result-count').textContent =
                'Errore rete, controlla connessione';
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
    document.getElementById('result-count').textContent = elements.length + ' risultati trovati';
}

// ── current clicked feature ────────────────────────────────
var currentFeature = null;


// ── close panel on X button ────────────────────────────────
document.getElementById('poi-close').addEventListener('click', function() {
    document.getElementById('poi-panel').style.display   = 'none';
    document.getElementById('poi-overlay').style.display = 'none';
    currentFeature = null;
});

// ── close panel on overlay click ──────────────────────────
document.getElementById('poi-overlay').addEventListener('click', function() {
    document.getElementById('poi-panel').style.display   = 'none';
    document.getElementById('poi-overlay').style.display = 'none';
    currentFeature = null;
});

// ── heart button toggles like ─────────────────────────────
document.getElementById('poi-heart-btn').addEventListener('click', function() {
    var cb = document.getElementById('poi-liked');
    cb.checked = !cb.checked;
    this.textContent = cb.checked ? '♥' : '♡';
    this.classList.toggle('liked', cb.checked);
});

// ── map click → open panel ────────────────────────────────
map.on('click', function(evt) {
    var feature = map.forEachFeatureAtPixel(evt.pixel, function(f) { return f; });

    if (feature) {
        currentFeature = feature;
        document.getElementById('poi-name').textContent      = feature.get('name');
        document.getElementById('poi-address').textContent   = feature.get('tags')['addr:street'] || '';
        document.getElementById('poi-comment').value         = '';
        document.getElementById('poi-liked').checked         = false;
        document.getElementById('poi-heart-btn').textContent = '♡';
        document.getElementById('poi-heart-btn').classList.remove('liked');
        document.getElementById('poi-save-msg').style.display  = 'none';
        document.getElementById('poi-panel').style.display    = 'block';
        document.getElementById('poi-overlay').style.display  = 'block';

        // check if already liked
        fetch('../LikedServlet?osm_id=' + feature.get('osm_id'))
            .then(function(res) { return res.json(); })
            .then(function(data) {
                document.getElementById('poi-liked').checked = data.is_liked;
                var btn = document.getElementById('poi-heart-btn');
                btn.textContent = data.is_liked ? '♥' : '♡';
                btn.classList.toggle('liked', data.is_liked);
            });
    } else {
        document.getElementById('poi-panel').style.display   = 'none';
        document.getElementById('poi-overlay').style.display = 'none';
        currentFeature = null;
    }
});

// ── save button ───────────────────────────────────────────
document.getElementById('poi-save-btn').addEventListener('click', function() {
    if (!currentFeature) return;

    var tags   = currentFeature.get('tags');
    var data   = new URLSearchParams();
    var coords = currentFeature.getGeometry().getCoordinates();
    var lonLat = ol.proj.toLonLat(coords);

    data.append('osm_id',   currentFeature.get('osm_id'));
    data.append('name',     currentFeature.get('name'));
    data.append('lon',      lonLat[0]);
    data.append('lat',      lonLat[1]);
    data.append('type',     tags['aeroway'] || tags['amenity'] || tags['tourism'] || 'unknown');
    data.append('comment',  document.getElementById('poi-comment').value.trim());
    data.append('is_liked', document.getElementById('poi-liked').checked ? 'true' : 'false');

    fetch('../PoiServlet', { method: 'POST', body: data })
        .then(function(res) { return res.text(); })
        .then(function(msg) {
            var el = document.getElementById('poi-save-msg');
            el.textContent = msg;
            el.style.display = 'block';
        })
        .catch(function(err) { console.error('Save error:', err); });
});

// add this variable at top
var searchTimeout = null;

document.getElementById('search-btn').addEventListener('click', function() {
    clearTimeout(searchTimeout);
    searchTimeout = setTimeout(searchPOI, 1100);  // 1.1 seconds
});

document.getElementById('city').addEventListener('keydown', function(e) {
    if (e.key === 'Enter') {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(searchPOI, 1100);  // 1.1 seconds
    }
});

// store departure and arrival
var partenzaFeature = null;
var arrivoFeature   = null;

function resetBookingUI() {

    partenzaFeature = null;
    arrivoFeature   = null;

    document.getElementById('val-partenza').textContent = '—';
    document.getElementById('val-arrivo').textContent   = '—';

    document.getElementById('booking-date').value = '';

    document.getElementById('slot-partenza').style.borderBottom = '';
    document.getElementById('slot-arrivo').style.borderBottom   = '';

    document.getElementById('booking-confirm-btn').disabled = true;

    document.getElementById('booking-msg').style.display = 'none';

    checkBookingReady();
}



function checkBookingReady() {
    var date = document.getElementById('booking-date').value;
    var ready = partenzaFeature && arrivoFeature && date;
    document.getElementById('booking-confirm-btn').disabled = !ready;
}

// set partenza
document.getElementById('poi-partenza-btn').addEventListener('click', function() {
    if (!currentFeature) return;

    // block same airport
    if (arrivoFeature && arrivoFeature.get('osm_id') === currentFeature.get('osm_id')) {
        alert('Partenza e arrivo non possono essere lo stesso aeroporto');
        return;
    }

    partenzaFeature = currentFeature;
    document.getElementById('val-partenza').textContent = currentFeature.get('name');
    document.getElementById('slot-partenza').style.borderBottom = '2px solid var(--accent2)';

    // close panel
    document.getElementById('poi-panel').style.display   = 'none';
    document.getElementById('poi-overlay').style.display = 'none';
    checkBookingReady();
});

// set arrivo
document.getElementById('poi-arrivo-btn').addEventListener('click', function() {
    if (!currentFeature) return;

    // block same airport
    if (partenzaFeature && partenzaFeature.get('osm_id') === currentFeature.get('osm_id')) {
        alert('Partenza e arrivo non possono essere lo stesso aeroporto');
        return;
    }

    arrivoFeature = currentFeature;
    document.getElementById('val-arrivo').textContent = currentFeature.get('name');
    document.getElementById('slot-arrivo').style.borderBottom = '2px solid var(--success)';

    // close panel
    document.getElementById('poi-panel').style.display   = 'none';
    document.getElementById('poi-overlay').style.display = 'none';
    checkBookingReady();
});

// RESET BUTTON
document.getElementById('booking-reset-btn')
    .addEventListener('click', function () {

        resetBookingUI();
    });
// date change
document.getElementById('booking-date').addEventListener('change', checkBookingReady);

// confirm booking
document.getElementById('booking-confirm-btn').addEventListener('click', function() {
    var date = document.getElementById('booking-date').value;
    if (!partenzaFeature || !arrivoFeature || !date) return;
    this.disabled = true;
    var data = new URLSearchParams();

    // partenza
    var c1 = ol.proj.toLonLat(partenzaFeature.getGeometry().getCoordinates());
    data.append('par_osm_id', partenzaFeature.get('osm_id'));
    data.append('par_airporto',   partenzaFeature.get('name'));
    data.append('par_lat',    c1[1]);
    data.append('par_lon',    c1[0]);

    // arrivo
    var c2 = ol.proj.toLonLat(arrivoFeature.getGeometry().getCoordinates());
    data.append('arr_osm_id',   arrivoFeature.get('osm_id'));
    data.append('arr_airporto',     arrivoFeature.get('name'));
    data.append('arr_lat',      c2[1]);
    data.append('arr_lon',      c2[0]);

    data.append('data', date);

    fetch('../PrenotazioneServlet', { method: 'POST', body: data })
        .then(function(res) { return res.text(); })
        .then(function(msg) {
            var el = document.getElementById('booking-msg');
            el.textContent = msg;
            el.style.display = 'block';
            el.style.color = msg.includes('confermata') ? 'var(--success)' : 'var(--error)';
            el.style.borderColor = msg.includes('confermata') ? 'var(--success)' : 'var(--error)';

            setTimeout(function() {
                el.style.display = 'none';

                if (msg.includes('confermata')) {
                    resetBookingUI();
                }

                checkBookingReady();

            }, 4000);
        })
        .catch(function(err) { console.error('Booking error:', err); });
});


function checkOverpassStatus(callback) {
    fetch('https://overpass-api.de/api/status')
        .then(function(res) { return res.text(); })
        .then(function(status) {
            // Parse status to find available slots
            if (status.includes('available now')) {
                callback(true);
            } else {
                document.getElementById('result-count').textContent =
                    'Server occupato, attendi 10 secondi...';
                setTimeout(function() { callback(true); }, 10000);
            }
        })
        .catch(function() {
            callback(true);  // proceed anyway if status check fails
        });
}
