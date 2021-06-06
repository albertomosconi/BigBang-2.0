function listSearched(keyword, items, viewed){
  var section = document.createElement('section');

searchMessage = new SearchMessage(items,keyword),
document.getElementById("keyword"),document.getElementById("number_items_search"));
searchMessage.show();

itemsCompressed = new CompressedItem(items)
itemsCompressed.show();




  var infoSearch = document.createElement('h2');
  infoSearch.textContent = 'Your search for '+keyword+ ' returned ' +items.size()+ ' results.';
  section.appendChild(infoSearch);
}

//after a search fill the first h2 of HTML whit this two info:
//the word searched and the number of item returned
//the two container are the attribute (id) of the two span in HTML page were to put these info
function SearchMessage(_items, _keyword, container1, container2){
  this.items = _items;
  this.keyword = _keyword;
  this.show = function(){
    container1.textContent = this.keyword;
    container2.textContent = this.items.size();
  }
}

function CompressedItem(listItems){
  var container = document.createElement('div');
  var itemContainer, id, name, price, viewButton;

  listItems.forEach((item, i) => {
    console.log(item);

    itemContainer=document.createElement('div');
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
    price.textContent = item['priceList'][0]['price'] + '&euro;'
    itemContainer.appendChild(price);

    //buttom for Visualized the item
    viewButton = document.createElement('button');
    viewButton.textContent = 'View';
    itemContainer.appendChild(viewButton);

    }
    container.appendChild(itemContainer);

  });
  return container;


  }

}
