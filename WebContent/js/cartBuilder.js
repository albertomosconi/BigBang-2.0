function buildCart(cart) {
  var container = document.createElement('div');
    console.log(cart.lenght);

    for(i = 0; i< cart.lenght; i++){

    
    var vendorContainer = document.createElement('div');
    vendorContainer.classList.add('vendor-section');
    vendorContainer.classList.add('card');

    var vendorHeader = document.createElement('div');
    vendorHeader.classList.add('vendor-head');

    var vendorName = document.createElement('div');
    vendorName.classList.add('vendor-name');
    vendorName.textContent(cart[i].vendorName);

    var vendorRate = document.createElement('div');
    vendorRate.classList.add('vendor-rate');

    vendorHeader.appendChild(vendorName);
    vendorContainer.appendChild(vendorHeader);

    container.appendChild(vendorContainer);
  }
  
  return container;
}