document.getElementById('action-btn').addEventListener('click', () => {
    const output = document.getElementById('output');
    const status = document.getElementById('status');

    console.log("Starting test...");

    status.innerText = "Processing...";
    status.style.color = "#d29922";

    setTimeout(() => {
        output.innerText = "> Script executed successfully at " + new Date().toLocaleTimeString();
        status.innerText = "Online";
        status.style.color = "#3fb950";

        document.getElementById('test-image').style.borderColor = '#79c0ff';
    }, 800);
});