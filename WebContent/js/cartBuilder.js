function buildCart(cart) {
  var container = document.createElement('div');
  console.log(cart.length);

  cart.forEach((vendor, i) => {
    // VENDOR NAME - VENDOR RATE
    var vendorContainer = document.createElement('div');
    vendorContainer.classList.add('vendor-section');
    vendorContainer.classList.add('card');

    var vendorHeader = document.createElement('div');
    vendorHeader.classList.add('vendor-head');

    var vendorName = document.createElement('div');
    vendorName.classList.add('vendor-name');
    vendorName.textContent = vendor.vendorName;

    var vendorRate = document.createElement('div');
    vendorRate.classList.add('vendor-rate');

    for (let r = 0; r < 5; r++) {
      var vendorStar = document.createElement('span');
      vendorStar.classList.add('fa', 'fa-star');
      if (r < vendor.vendorScore) vendorStar.classList.add('checked');
      vendorRate.appendChild(vendorStar);
    }

    vendorHeader.appendChild(vendorName);
    vendorHeader.appendChild(vendorRate);

    var tableItem = document.createElement('table');
    tableItem.classList.add('table-item');

    vendor.items.forEach((item, j) => {
      //ITEM NAME - ITEM PRICE - ITEM QUANTITY - ITEM TOTAL

      var itemRow = document.createElement('tr');
      itemRow.classList.add('item');

      var itemName = document.createElement('td');
      itemName.classList.add('item-name');
      itemName.textContent = item.itemName;

      var itemPrice = document.createElement('td');
      itemPrice.classList.add('price-setting-bis');
      itemPrice.textContent =
        Intl.NumberFormat('de-DE', {
          style: 'currency',
          currency: 'EUR',
        }).format(item.price) + '/cad.';

      var itemQuantity = document.createElement('td');
      itemQuantity.classList.add('item-quantity');

      var decrementButton = document.createElement('button');
      //decrementButton.type('submit');
      decrementButton.classList.add('increment-decrement-button');
      decrementButton.textContent = '-';
      decrementButton.addEventListener('click', (e) => {
        e.preventDefault();
        doAddCart(vendor.vendorId, item.itemId, null, true);
      });

      itemQuantity.appendChild(decrementButton);

      var quantityNumber = document.createElement('div');
      quantityNumber.textContent = item.quantity;
      quantityNumber.classList.add('item-number');

      itemQuantity.appendChild(quantityNumber);

      var incrementButton = document.createElement('button');
      //incrementButton.type('submit');
      incrementButton.classList.add('increment-decrement-button');
      incrementButton.textContent = '+';
      incrementButton.addEventListener('click', (e) => {
        e.preventDefault();
        doAddCart(vendor.vendorId, item.itemId, null, null);
      });

      itemQuantity.appendChild(incrementButton);

      var itemPriceTotal = document.createElement('td');
      itemPriceTotal.classList.add('item-amount');
      itemPriceTotal.classList.add('prices-setting');
      itemPriceTotal.textContent = Intl.NumberFormat('de-DE', {
        style: 'currency',
        currency: 'EUR',
      }).format(item.price * item.quantity);

      itemRow.appendChild(itemName);
      itemRow.appendChild(itemPrice);
      itemRow.appendChild(itemQuantity);
      itemRow.appendChild(itemPriceTotal);

      tableItem.appendChild(itemRow);
    });

    var footer = document.createElement('div');
    footer.classList.add('footer');

    var footerText = document.createElement('div');
    footerText.classList.add('text');

    var subtotalText = document.createElement('div');
    subtotalText.classList.add('subtotal');
    subtotalText.textContent = 'Sub-Total: ';

    var shippingText = document.createElement('div');
    shippingText.classList.add('shipping');
    shippingText.textContent = 'Shipping: ';

    var totalText = document.createElement('div');
    totalText.classList.add('total');
    totalText.textContent = 'Total: ';

    footerText.appendChild(subtotalText);
    footerText.appendChild(shippingText);
    footerText.appendChild(totalText);

    var footerPrice = document.createElement('div');
    footerPrice.classList.add('prices');

    var subtotalPrice = document.createElement('div');
    subtotalPrice.classList.add('subtotal-price');
    subtotalPrice.textContent = Intl.NumberFormat('de-DE', {
      style: 'currency',
      currency: 'EUR',
    }).format(vendor.subtotal);

    var shippingPrice = document.createElement('div');
    shippingPrice.classList.add('shipping-price');
    shippingPrice.textContent = Intl.NumberFormat('de-DE', {
      style: 'currency',
      currency: 'EUR',
    }).format(vendor.shipping);

    var totalPrice = document.createElement('div');
    totalPrice.classList.add('total');
    totalPrice.textContent = Intl.NumberFormat('de-DE', {
      style: 'currency',
      currency: 'EUR',
    }).format(vendor.total);

    var form = document.createElement('form');
    form.classList.add('order-cart-button');

    var orderButton = document.createElement('button');
    orderButton.classList.add('order-cart');
    orderButtonSpan = document.createElement('span');
    orderButtonSpan.textContent = 'Order';
    orderButton.appendChild(orderButtonSpan);
    orderButton.addEventListener('click', (e) => {
      e.preventDefault();
      doOrders(vendor.vendorId);
    });

    form.appendChild(orderButton);

    footerPrice.appendChild(subtotalPrice);
    footerPrice.appendChild(shippingPrice);
    footerPrice.appendChild(totalPrice);

    footer.appendChild(footerText);
    footer.appendChild(footerPrice);

    vendorContainer.appendChild(tableItem);
    vendorContainer.appendChild(vendorHeader);
    vendorContainer.appendChild(tableItem);
    vendorContainer.appendChild(footer);
    vendorContainer.appendChild(form);

    container.appendChild(vendorContainer);
  });

  return container;
}
