function buildCart(cart) {
  var container = document.createElement('div');

  	cart.forEach((vendor, index) => {
    
    var vendorContainer = document.createElement('div');
    vendorContainer.classList.add('vendor-section');
    vendorContainer.classList.add('card');

 	var vendorHeader = document.createElement('div');
    vendorHeader.classList.add('vendor-head');
	container.appendChild(vendorContainer);
  });
  return container;
}
