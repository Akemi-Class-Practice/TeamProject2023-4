function calculateTotal() {
    var prices = document.getElementsByClassName('lecture-price');
    var total = 0;

    for (var i = 0; i < prices.length; i++) {
        var price = parseFloat(prices[i].textContent.replace(',', '').replace('Pt', ''));
        total += price;
    }

    return total;
}


var totalAmountElement = document.getElementById('total-amount');
var totalAmount = calculateTotal();
totalAmountElement.textContent = formatAmount(totalAmount) + 'Pt';

function formatAmount(amount) {
    return amount.toLocaleString();
}

