function goHome() {
  console.log('go home');
  var errorContainer = document.getElementById('errorMessage');
  errorContainer.style.display = 'none';
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
  //clear error container
  var errorContainer = document.getElementById('errorMessage');
  errorContainer.style.display = 'none';

  document.getElementById('pageContainer').innerHTML = '';

  var pageContainer = document.getElementById('pageContainer');

  // create heading
  var heading = document.createElement('h1');
  heading.textContent = 'Items in your cart';
  heading.classList.add('title');
  var icon = document.createElement('div');
  icon.innerHTML = `<i class="fa fa-shopping-cart"></i>`;
  heading.appendChild(icon);

  pageContainer.appendChild(heading);

  // create cart
  var cartString = window.sessionStorage.getItem('cartSession');
  if (cartString != null) {
    var cart = JSON.parse(cartString);
    var cartContainer = buildCart(cart);
    pageContainer.appendChild(cartContainer);
  }
}

function doSearch(keyword, viewed = null) {
  var errorContainer = document.getElementById('errorMessage');
  errorContainer.style.display = 'none';
  //function called after press send in search input
  doRequest('search?keyword=' + keyword, 'GET', (req) => {
    if (req.readyState == XMLHttpRequest.DONE) {
      var responseBody = req.responseText;
      switch (req.status) {
        case 200:
          document.getElementById('pageContainer').innerHTML = '';
          // request was successful, go to search page
          var itemsSearch = JSON.parse(responseBody);

          var pageContainer = document.getElementById('pageContainer');
          //items list
          var searchContainer = listSearched(itemsSearch);
          pageContainer.appendChild(searchContainer);
          //top button
          var topButton = document.createElement('button');
          topButton.id = 'topBtn';
          topButton.classList.add('topBtn');
          topButton.textContent = 'Top';
          // When the user scrolls down 20px from the top of the document, show the button
          window.onscroll = function () {
            scrollFunction();
          };
          // When the user clicks on the button, scroll to the top of the document
          topButton.addEventListener('click', (e) => {
            e.preventDefault;
            document.body.scrollTop = 0;
            document.documentElement.scrollTop = 0;
          });
          pageContainer.appendChild(topButton);
          break;

        default:
          // request failed, display error
          var errorContainer = document.getElementById('errorMessage');
          errorContainer.style.display = 'block';
          document.getElementById('errorBody').textContent = responseBody;
          break;
      }
    }
  });
}

function doView(idItem, item) {
  var errorContainer = document.getElementById('errorMessage');
  errorContainer.style.display = 'none';
  //function called press the view button of an item
  doRequest('visualize?idItem=' + idItem, 'POST', (req) => {
    if (req.readyState == XMLHttpRequest.DONE) {
      var responseBody = req.responseText;
      switch (req.status) {
        case 200:
          // visualized the item requested
          buildExtendedItemBox(idItem, item);
          break;

        default:
          // request failed, display error
          var errorContainer = document.getElementById('errorMessage');
          errorContainer.style.display = 'block';
          document.getElementById('errorBody').textContent = responseBody;
          break;
      }
    }
  });
}

function doOrders(vendorId) {
  var errorContainer = document.getElementById('errorMessage');
  errorContainer.style.display = 'none';
  doRequest('doOrder?vendorId=' + vendorId, 'POST', (req) => {
    if (req.readyState == XMLHttpRequest.DONE) {
      let responseBody = req.responseText;
      switch (req.status) {
        case 200:
          document.getElementById('pageContainer').innerHTML = '';
          let cartString = window.sessionStorage.getItem('cartSession');
          if (cartString != null) {
            let cart = JSON.parse(cartString);
            let newCart = [];
            cart.forEach((vendor) => {
              if (vendor.vendorId !== vendorId) newCart.push(vendor);
            });
            window.sessionStorage.setItem(
              'cartSession',
              JSON.stringify(newCart),
            );
          }
          goOrders();
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

function goOrders() {
  var errorContainer = document.getElementById('errorMessage');
  errorContainer.style.display = 'none';
  doRequest('orders', 'GET', (req) => {
    if (req.readyState == XMLHttpRequest.DONE) {
      let responseBody = req.responseText;
      switch (req.status) {
        case 200:
          document.getElementById('pageContainer').innerHTML = '';
          // request successful
          let orders = JSON.parse(responseBody);
          let pageContainer = document.getElementById('pageContainer');
          console.log(orders);
          // create heading
          let heading = document.createElement('h1');
          heading.textContent = 'Your orders';
          pageContainer.appendChild(heading);
          // create list
          let listContainer = buildOrdersList(orders);
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

function doLogout() {
  var errorContainer = document.getElementById('errorMessage');
  errorContainer.style.display = 'none';
  //function called after push the logout button
  doRequest('logout', 'GET', (req) => {
    if (req.readyState == XMLHttpRequest.DONE) {
      switch (req.status) {
        case 200:
          // request was successful, go to login page
          //clear the sessionStorage
          sessionStorage.clear();
          window.location.href = 'login.html';
          break;

        default:
          var errorContainer = document.getElementById('errorMessage');
          errorContainer.style.display = 'block';
          document.getElementById('errorBody').textContent = responseBody;
          break;
      }
    }
  });
}

function doAddCart(vendor, item, quantity, sub) {
  var path = 'doAddCart?vendorId=' + vendor + '&itemId=' + item;

  if (quantity != null) path += '&quantity=' + quantity;
  if (sub != null && sub == true) path += '&sub=true';

  //clear error container
  var errorContainer = document.getElementById('errorMessage');
  errorContainer.style.display = 'none';

  doRequest(path, 'POST', (req) => {
    if (req.readyState == XMLHttpRequest.DONE) {
      var responseBody = req.responseText;
      switch (req.status) {
        case 200:
          document.getElementById('pageContainer').innerHTML = '';
          // request was successful, go to cart page
          window.sessionStorage.setItem('cartSession', responseBody);
          var cart = JSON.parse(responseBody);
          var pageContainer = document.getElementById('pageContainer');

          // create headind
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
          pageContainer.innerHTML = '';
          var heading = document.createElement('h1');
          heading.textContent = 'Items in your cart';
          pageContainer.appendChild(heading);

        default:
          // request failed, display error
          var errorContainer = document.getElementById('errorMessage');
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
