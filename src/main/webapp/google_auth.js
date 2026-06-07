function handleGoogleResponse(response) {
    const token = response.credential;

    fetch('GoogleAuthServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ token: token })
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                window.location.href = data.redirect;
            } else {
                alert('Google login fallito. Riprova.');
            }
        })
        .catch(err => {
            console.error('Errore:', err);
            alert('Errore di connessione.');
        });
}