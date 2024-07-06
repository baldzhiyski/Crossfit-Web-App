let currencyDropDown = document.getElementById('currency')

currencyDropDown.addEventListener('change', currencyChange)

function currencyChange() {
    let selectedCurrency = currencyDropDown.value
    let priceElements = document.querySelectorAll('.price');
    let priceInBGNElements = document.querySelectorAll('.priceInBGN');

    priceInBGNElements.forEach((priceInBGNElement, index) => {
        let amountInBGN = priceInBGNElement.value;
        let priceElement = priceElements[index];

        fetch('/crossfit/api/convert?' + new URLSearchParams({
            from: 'USD',
            to: selectedCurrency,
            amount: amountInBGN
        }))
            .then(response => response.json())
            .then(data => {
                priceElement.textContent = data.result + ' ' + selectedCurrency + '/month';
            })
            .catch(error => {
                console.log('An error occurred: ' + error);
            });
    });
}