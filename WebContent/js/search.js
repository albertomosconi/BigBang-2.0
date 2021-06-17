function listSearched(items){
  var section = document.createElement('section');
  var keyword = sessionStorage.getItem('keyword');
  var viewed = sessionStorage.getItem('viewItem');

  var infoSearch = document.createElement('h2');
  infoSearch.textContent =
    'Your search for "' + keyword + '" returned ' + items.length + ' results.';
  section.appendChild(infoSearch);

  var itemsContainer = document.createElement('div');
  console.log(`items`, items.length);

if (viewed === null) {
  // no item visualized in the page
  items.forEach((item, i) => {
    var compressedId = document.createElement('div');
    compressedId.id = item['id'];
    var singleContainer = new CompressedItem(item);
    compressedId.appendChild(singleContainer);
    itemsContainer.appendChild(compressedId);
    //itemsCompressed.show();
  });
}
else{
  items.forEach((item, i) => {
    if (viewed.includes(item.id)) {
      //this item is Visualized
      var singleContainer = buildExtendedItem(item);
      itemsContainer.appendChild(singleContainer);
    } else {
      var singleContainer = new CompressedItem(item);
      itemsContainer.appendChild(singleContainer);
    }
  });
}
section.appendChild(itemsContainer);
return section;
}



//after a search fill the first h2 of HTML whit this two info:
//the word searched and the number of item returned
//the two container are the attribute (id) of the two span in HTML page were to put these info
function SearchMessage(_items, _keyword, container1, container2) {
  this.items = _items;
  this.keyword = _keyword;
  this.show = function () {
    container1.textContent = this.keyword;
    container2.textContent = this.items.size();
  };
}

function CompressedItem(item) {
  //var container = document.createElement('div');
  var itemContainer, id, name, price, viewButton;
  console.log(item);

  itemContainer = document.createElement('div');
  itemContainer.classList.add('item-card');
  itemContainer.classList.add('card');

  // item ID
  id = document.createElement('h1');
  id.classList.add('item-id');
  id.textContent = item['id'];
  itemContainer.appendChild(id);

  // item NAME
  name = document.createElement('h1');
  name.classList.add('item-name');
  name.textContent = item['name'];
  itemContainer.appendChild(name);

  // item lower PPRICE
  price = document.createElement('h1');
  price.classList.add('item-price');
  price.textContent = item['priceList'][0]['price'] + '€';
  itemContainer.appendChild(price);

  //buttom for Visualized the item
  viewButton = document.createElement('button');
  viewButton.classList.add('view-button');
  viewButton.textContent = 'View';
  itemContainer.appendChild(viewButton);
  viewButton.addEventListener('click', (e) =>{

    //call the POST in the server
    doView(id.textContent, item);

  })

  // container.appendChild(itemContainer);

  return itemContainer;
}

function buildExtendedItemBox(idItem, item){
  //add the itemId visualized in the session sessionStorage
  var visualized = sessionStorage.getItem('viewItem');
  if (visualized == null) {
    visualized = new Array();
  }
  else{
    visualized = JSON.parse("[" + visualized + "]");
  }
  visualized.push(idItem);
  sessionStorage.setItem('viewItem', visualized);

  //now make the item visualized(extended)
  var itemContainer = document.getElementById(idItem);
  itemContainer.innerHTML = "";
  var extendedItem = buildExtendedItem(item);
  itemContainer.appendChild(extendedItem);
}

function ExtendedItem(item){
  console.log(item);
  var image, name, category, description, vendors;
  var container = document.createElement('div');
  container.classList.add('item-card');
  container.classList.add('card');

  // item img
  image = document.createElement('img');
  image.src = './images/products/' + item['picture'];
  image.alt = 'item picture';
  container.appendChild(image);

  // item info --> inner item container
  var productInfo = document.createElement('div');
  productInfo.classList.add('product-info');

  // item NAME
  name = document.createElement('h1');
  name.classList.add('item-name');
  name.textContent = item['name'];
  productInfo.appendChild(name);

  // item category
  category = document.createElement('span');
  category.classList.add('product-category');
  category.textContent = item['category'];
  productInfo.appendChild(productCategory);

  // product description
  description = document.createElement('p');
  description.classList.add('product-description');
  description.textContent = item['description'];
  productInfo.appendChild(productDescription);

  container.appendChild(productInfo);

  // item vendors
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

  container.appendChild(vendors);

  return container;
}
