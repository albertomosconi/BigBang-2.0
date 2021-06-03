function buildLastViewed(items) {
  var container = document.createElement('div');

  items.forEach((item, i) => {
    console.log(item);
    var itemContainer = document.createElement('div');
    itemContainer.classList.add('item-card');
    itemContainer.classList.add('card');

    var image = document.createElement('img');
    image.src = './images/products/' + item['picture'];
    image.alt = 'item picture';
    itemContainer.appendChild(image);

    // PRODUCT INFO
    var productInfo = document.createElement('div');
    productInfo.classList.add('product-info');
    // product name
    var productTitle = document.createElement('h1');
    productTitle.classList.add('product-title');
    productTitle.textContent = item['name'];
    productInfo.appendChild(productTitle);
    // product category
    var productCategory = document.createElement('span');
    productCategory.classList.add('product-category');
    productCategory.textContent = item['category'];
    productInfo.appendChild(productCategory);
    // product description
    var productDescription = document.createElement('p');
    productDescription.classList.add('product-description');
    productDescription.textContent = item['description'];
    productInfo.appendChild(productDescription);
    itemContainer.appendChild(productInfo);

    // PRODUCT VENDORS
    var vendors = document.createElement('div');
    vendors.classList.add('vendors');

    for (let i = 0; i < item['vendorList'].lenght; i++) {
      var itemVendor = document.createElement('div');
      itemVendor.classList.add('item-vendor');

      var priceRow = document.createElement('div');
      priceRow.classList.add('item-price-row');
      var itemPrice = document.createElement('span');
      itemPrice.classList.add('item-price');
      itemPrice.textContent = item['priceList'][i]['price'] + '&euro;';
      priceRow.appendChild(itemPrice);

      var form = document.createElement('form');
      // link form to add to cart method
      var quantityInput = document.createElement('input');
      quantityInput.type = 'number';
      quantityInput.min = 1;
      quantityInput.name = 'quantity';
      quantityInput.value = 1;
      form.appendChild(quantityInput);
      var addButton = document.createElement('button');
      addButton.textContent = 'Add to cart';
      form.appendChild(addButton);
      priceRow.appendChild(form);

      itemVendor.appendChild(priceRow);

      vendors.appendChild(itemVendor);
    }
    itemContainer.appendChild(vendors);

    container.appendChild(itemContainer);
  });
  return container;
}
