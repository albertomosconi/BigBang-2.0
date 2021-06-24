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
  });
}
else{
    //the vield with all id items visualized is not empty
  items.forEach((item, i) => {
    if (viewed.includes(item.id)) {
      //this item is Visualized
      var singleContainer = buildExtendedItem(item);
      itemsContainer.appendChild(singleContainer);
    } else {
      //this one not
      var singleContainer = new CompressedItem(item);
      itemsContainer.appendChild(singleContainer);
    }
  });
}
section.appendChild(itemsContainer);
return section;
}


function CompressedItem(item) {

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
  var viewDiv = document.createElement('div');
  viewDiv.id = 'view';
  viewDiv.classList.add('view');
  viewButton = document.createElement('button');
  viewButton.classList.add('view-button');

  var span = document.createElement('SPAN');
  span.textContent = 'View';
  //viewDiv.appendChild(span);

  viewButton.appendChild(span);
  viewDiv.appendChild(viewButton);
  itemContainer.appendChild(viewDiv);
  
  viewButton.addEventListener('click', (e) =>{

    //call the POST in the server
    doView(id.textContent, item);

  })

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
