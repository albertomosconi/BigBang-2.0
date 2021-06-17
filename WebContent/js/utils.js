function doRequest(url, method, callback, form = null) {
  console.log(form);
  var request = new XMLHttpRequest();
  request.onreadystatechange = () => callback(request);
  request.open(method, url);
  if (form == null) {
    request.send();
  } else {
    var fd = new FormData(form);
    request.send(fd);
  }
}

function linkButton(id, cb) {
  var button = document.getElementById(id);
  button.addEventListener('click', (e) => {
    e.preventDefault();
    cb();
  });
}

function initializeHome() {
  // hide error container from page
  var errorContainer = document.getElementById('errorMessage');
  errorContainer.style.display = 'none';

  // fill username text in top right
  var userString = sessionStorage.getItem('user');
  var user = JSON.parse(userString);

  var navUsername = document.getElementById('navUsername');
  navUsername.textContent = user['name'] + ' ' + user['surname'];

  // link nav buttons
  linkButton('goHomeLink', () => goHome());
  linkButton('goCartLink', () => goCart());
  linkButton('goOrdersLink', () => goOrders());
  linkButton('doLogoutLink', () => doLogout());

  //search form
  var keyword = document.getElementById('keyword');


  keyword.addEventListener("keydown", (e) =>{
    if (e.code === "Enter") {
      e.preventDefault();
      //save the keyword in the sessionStorage, and remove the old visualized
    sessionStorage.setItem('keyword', keyword.value);
    //var v = '';
    sessionStorage.removeItem('viewItem');
      console.log(keyword);
      if (keyword.checkValidity()) {
        //call the method that handle the search
        var searchForm = document.getElementById('searchForm');
        console.log(searchForm);

        doSearch(keyword.value);
         keyword.value = '';
      } else {
        errorContainer.style.display = 'block';
        document
          .getElementById('errorBody')
          .textContent('Error in Input string');
      }
    }
  });


}

function buildExtendedItem(item) {
  console.log(`building extended item`, item);
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

  for (let i = 0; i < item.vendorList.length; i++) {
    var itemVendor = document.createElement('div');
    itemVendor.classList.add('item-vendor');

    var priceRow = document.createElement('div');
    priceRow.classList.add('item-price-row');
    var itemPrice = document.createElement('span');
    itemPrice.classList.add('item-price');
    itemPrice.textContent = item['priceList'][i]['price'] + '€';
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

    var soldByText = document.createElement('p');
    soldByText.innerHTML = 'sold by <strong>' + item['vendorList'][i]['name'];
    var ratingContainer = document.createElement('div');
    for (let r = 0; r < 5; r++) {
      var vendorStar = document.createElement('span');
      vendorStar.classList.add('fa', 'fa-star');
      if (r < item.vendorList[i].score) vendorStar.classList.add('checked');
      ratingContainer.appendChild(vendorStar);
    }
    soldByText.appendChild(ratingContainer);
    itemVendor.appendChild(soldByText);

    var itemsInCartText = document.createElement('p');

      var cartString = sessionStorage.getItem('cartSession');
      var cartJson = JSON.parse(cartString);
      var total = 0;
      var cartSummary = document.createElement("span");
      if(cartJson != null){
      cartJson.forEach((vendorCart, k)=>{
        if(vendorCart.vendorId == item['vendorList'][i]['id']){
          subtotalPrice = 0;
          vendorCart.items.forEach((itemCart, j) => {
            total = total + itemCart.quantity;
            subtotalPrice = subtotalPrice + itemCart.price * itemCart.quantity;
            var cartItem = document.createElement("span");
            cartItem.textContent = itemCart.quantity + " x " + itemCart.itemName;
            cartSummary.appendChild(cartItem);
            cartSummary.appendChild(document.createElement('br'));
        });
        var cartSubtotal = document.createElement("span");
        cartSubtotal.classList.add("cartSubtotal");
        cartSubtotal.textContent = "Subtotal " + Intl.NumberFormat('de-DE', { style: 'currency', currency: 'EUR' }).format(subtotalPrice);
        cartSummary.appendChild(cartSubtotal);
      }
      });
    }

  var itemsInCartStrong = document.createElement('strong');
  itemsInCartStrong.textContent = total + " items ";
  var itemsInCart = document.createElement('span');
  itemsInCart.textContent = 'in your cart are sold by this vendor';
  itemsInCartText.appendChild(itemsInCartStrong);
  itemsInCartText.appendChild(itemsInCart);

  //var popup = document.createElement('div');
  itemsInCartText.classList.add("popup");

  var popupContainer = document.createElement('span');
  popupContainer.classList.add('popuptext');
  popupContainer.id = 'myPopup'+item['vendorList'][i]['id']+item['id'];

if(cartSummary.children.length == 0){
  cartSummary.textContent="No item in your cart are sold by this vendor :( ";
}
popupContainer.appendChild(cartSummary);

    itemsInCartText.addEventListener('mouseover',(e)=>{
      var popup = document.getElementById("myPopup"+item['vendorList'][i]['id']+item['id']);
      popup.classList.toggle("show");
    });

    itemsInCartText.addEventListener('mouseout',(e)=>{
      var popup = document.getElementById("myPopup"+item['vendorList'][i]['id']+item['id']);
      popup.classList.toggle("show");
    });

    itemsInCartText.appendChild(popupContainer);
    itemVendor.appendChild(itemsInCartText);
    itemVendor.appendChild(document.createElement('br'));
    itemVendor.appendChild(document.createElement('br'));

    var shippingCostTitle = document.createElement('h4');
    shippingCostTitle.textContent = 'Shipping Cost';
    itemVendor.appendChild(shippingCostTitle);

    var shippingTable = document.createElement('table');
    for (let j = 0; j < item.vendorList[i].ranges.length; j++) {
      var range = item.vendorList[i].ranges[j];
      var tr = document.createElement('tr');
      var td1 = document.createElement('td');
      td1.textContent = range.min + ' to ' + range.max + ' items';
      var td2 = document.createElement('td');
      td2.textContent = range.cost + ' €';
      tr.appendChild(td1);
      tr.appendChild(td2);
      shippingTable.appendChild(tr);
    }
    itemVendor.appendChild(shippingTable);

    var freeShippingText = document.createElement('strong');
    freeShippingText.textContent =
      'FREE SHIPPING after ' + item.vendorList[i].free_limit + ' €';
    itemVendor.appendChild(freeShippingText);

    vendors.appendChild(itemVendor);
  }
  itemContainer.appendChild(vendors);
  return itemContainer;
}
