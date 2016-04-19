// ==UserScript==
// @name         Manning Reviews Formatter
// @namespace    http://www.imlovinit.fr/
// @version      0.1
// @description  Format book reviews in JSON
// @author       Julien Sobczak
// @match        https://www.manning.com/books/*
// @grant        GM_xmlhttpRequest
// ==/UserScript==
/* jshint -W097 */
'use strict';

// We count the matching reviews to display a warning message if no review at all satisfy the criteria
var countResult = 0;



function sendReview(book, quote, author, principal, totalReviews, index) {
    
    var indexBy = author.indexOf('by');
    if (indexBy === -1) indexBy = 0;
    var indexComma = author.indexOf(','); 
    var authorName = author.substring(indexBy, indexComma);
    
    var reviewTxt = "";
    reviewTxt +=  "# " + quote + "\n\n";
    reviewTxt += "<!--\n";
    reviewTxt += "  -- author: " + authorName + "\n";
    reviewTxt += "  -- id: Manning-" + index + "\n"; // HACK to support current model
    reviewTxt += "  -- book: " + book + "\n"; 
    reviewTxt += "  -- note: " + 5 + "\n";
    reviewTxt += "  -- votes: " + 0 + "\n";
    reviewTxt += "  -- position: " + (principal ? 1 : 2) + "\n";
    reviewTxt += "  -- total: " + totalReviews + "\n";
    reviewTxt += "  -->\n\n";
    reviewTxt += quote + "\n";

    GM_xmlhttpRequest({
      method: "POST",
      url: "http://localhost:8080/api/import",
      data: reviewTxt,
      headers: {
        "Content-Type": "text/plain"
      },
      onload: function(response) {
          countResult++;    
          if (countResult === totalReviews) {
              var body = document.getElementsByTagName('body')[0];
              var info = document.createElement('div');
              info.innerHTML = "OK";
              info.style.position = 'fixed';
              info.style.bottom = '0';
              info.style.left = '0';
              info.style.right = '0';
              info.style.height = '30px';
              info.style.zIndex = '100';
              info.style.padding = '5px';
              info.style.textAlign = 'center';
              info.style.backgroundColor = 'green';
              info.style.border = "2px solid black";
              body.appendChild(info);
          }
      }
    });

}


var book = document.querySelector('div.product-title').innerText.trim();
var principal = document.querySelector('blockquote.large-book-quote');
var secondaries = document.querySelectorAll('blockquote.small-book-quote');

var index = 0; // Index of the current review to generate a unique ID (used server-side to generate unique filenames)
var totalReviews = 1 + secondaries.length;

// Process the principal quote
var textReview = principal.children[0].innerText.trim();
var author = principal.children[1].innerText.trim();
sendReview(book, textReview, author, true, totalReviews, ++index);

// Process the secondary quotes
for (var i = 0; i < secondaries.length; i++) {
    var secondary = secondaries[i];
    var textReview = secondary.children[0].innerText.trim();
    var author = secondary.children[1].innerText.trim();   
    sendReview(book, textReview, author, false, totalReviews, ++index);
}




