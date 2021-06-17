function goHome() {
  console.log('go home');
  doRequest('view', 'GET', (req) => {
    if (req.readyState == XMLHttpRequest.DONE) {
      var responseBody = req.responseText;
      document.getElementById('pageContainer').innerHTML = '';
      switch (req.status) {
        case 200:
          // request was successful, go to home page
          var items = JSON.parse(responseBody);

          var pageContainer = document.getElementById('pageContainer');
          // create heading
          var heading = document.createElement('h1');
          heading.textContent = 'Last viewed items';
          pageContainer.appendChild(heading);
          // create list
          var listContainer = buildLastViewed(items);
          pageContainer.appendChild(listContainer);
          break;

        default:
          // request failed, display error
          errorContainer.style.display = 'block';
          document.getElementById('errorBody').textContent = responseBody;
          break;
      }
    }
  });
}

function goCart() {
  doRequest('cart', 'GET', (req) => {
    if (req.readyState == XMLHttpRequest.DONE) {
      var responseBody = req.responseText;
      document.getElementById('pageContainer').innerHTML = '';
      switch (req.status) {
        case 200:
          // request was successful, go to cart page
          //ripetitivo--> eliminare quando finisco il debug
          window.sessionStorage.setItem("cartSession",responseBody);
          var cart = JSON.parse(responseBody);
          var pageContainer = document.getElementById('pageContainer');

          // create heading
          // document.getElementById('pageContainer').innerHTML = '';
          var heading = document.createElement('h1');
          heading.textContent = 'Items in your cart';
          heading.classList.add('title');
          var icon = document.createElement('div');
          icon.innerHTML = `<i class="fa fa-shopping-cart"></i>`;
          heading.appendChild(icon);

          pageContainer.appendChild(heading);

          console.log(cart);
          // create cart
          var cartContainer = buildCart(cart);
          pageContainer.appendChild(cartContainer);
          break;

        case 204:
          // create heading
          var pageContainer = document.getElementById('pageContainer');
          document.getElementById('pageContainer').innerHTML = '';
          var heading = document.createElement('h1');
          heading.textContent = 'Items in your cart';
          pageContainer.appendChild(heading);
          break;

        default:
          // request failed, display error
          errorMessage.style.display = 'block';
          document.getElementById('errorBody').textContent = responseBody;
          break;
      }
    }
  });
}

function doSearch(keyword, viewed = null) {
  console.log(keyword);
  doRequest('search?keyword=' + keyword, 'GET', (req) => {
    if (req.readyState == XMLHttpRequest.DONE) {
      var responseBody = req.responseText;
      document.getElementById('pageContainer').innerHTML = '';
      switch (req.status) {
        case 200:
          // request was successful, go to search page
          if (!!responseBody) {
            //create an empty Array
            var itemsSearch = new Array();
          }
          else{
            //the response is not empty
            var itemsSearch = JSON.parse(responseBody);
          }

          var pageContainer = document.getElementById('pageContainer');

          console.log(itemsSearch, itemsSearch.length);

        var searchContainer = listSearched(itemsSearch);
        pageContainer.appendChild(searchContainer);
        break;
        }
    }



  });
}

function doView(idItem, item){
  doRequest('view?idItem=' +idItem, 'POST', (req) =>{
    if (req.readyState == XMLHttpRequest.DONE) {
    var responseBody = req.responseText;
    switch (req.status) {
      case 200:
      //request was successfully, go to search page with the new visualized
      /*
      var pageContainer = document.getElementById('pageContainer');
        var searchContainer = listSearched(keyword, itemsSearch, idItem);
        pageContainer.appendChild(searchContainer);*/
        buildExtendedItemBox(idItem, item);
        break;

      default:
      // request failed, display error
      errorContainer.style.display = 'block';
      document.getElementById('errorBody').textContent = responseBody;
      break;

    }
  }
  });
}

function doOrders() {
  alert('do orders');
}

function goOrders() {
  alert('go orders');
}

function doLogout() {
  alert('do logout');
}

function doAddCart(vendor, item, quantity, sub) {
  var path = 'doAddCart?vendorId=' + vendor + '&itemId=' + item;

  if (quantity != null) path += '&quantity=' + quantity;
  if (sub != null && sub == true) path += '&sub=true';

  doRequest(path, 'POST', (req) => {
    if (req.readyState == XMLHttpRequest.DONE) {
      var responseBody = req.responseText;
      switch (req.status) {
        case 200:
          // request was successful, go to cart page
          window.sessionStorage.setItem("cartSession",responseBody);
          var cart = JSON.parse(responseBody);
          var pageContainer = document.getElementById('pageContainer');

          // create heading
          document.getElementById('pageContainer').innerHTML = '';
          var heading = document.createElement('h1');
          heading.textContent = 'Items in your cart';
          heading.classList.add('title');
          var icon = document.createElement('div');
          icon.innerHTML = `<i class="fa fa-shopping-cart"></i>`;
          heading.appendChild(icon);

          pageContainer.appendChild(heading);

          console.log(cart);
          // create cart
          var cartContainer = buildCart(cart);
          pageContainer.appendChild(cartContainer);
          break;

        case 204:
          // create heading
          var pageContainer = document.getElementById('pageContainer');
          document.getElementById('pageContainer').innerHTML = '';
          var heading = document.createElement('h1');
          heading.textContent = 'Items in your cart';
          pageContainer.appendChild(heading);

        default:
          // request failed, display error
          errorContainer.style.display = 'block';
          document.getElementById('errorBody').textContent = responseBody;
          break;
      }
    }
  });
}

(function () {
  window.addEventListener('load', (e) => {
    // initialize event listeners
    initializeHome();
    // display home page
    goHome();
  });
})();
