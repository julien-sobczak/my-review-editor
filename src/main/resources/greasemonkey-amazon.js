// ==UserScript==
// @name         Amazon Reviews Formatter
// @namespace    http://www.imlovinit.fr/
// @version      0.1
// @description  Format book reviews in Markdown
// @author       Julien Sobczak
// @match        http://www.amazon.com/*product-reviews/*
// @grant        GM_xmlhttpRequest
// ==/UserScript==
/* jshint -W097 */
'use strict';

var productTitle = document.getElementsByClassName('product-title')[0].innerText.trim();

var totalReviews = document.getElementsByClassName('totalReviewCount')[0].innerHTML;
console.log(totalReviews, ' total reviews');


// We count the matching reviews to display a warning message if no review at all satisfy the criteria
var count_result = 0;

var reviews = document.getElementsByClassName('review');
var allReviewTxt = '';

try {
for (var i = 0; i < reviews.length; i++) {

    var review = reviews[i];  
    var reviewId = review.id;

    var headline = review.getElementsByClassName('review-title')[0].innerHTML;
    var text = review.getElementsByClassName('review-text')[0].innerHTML;
    text = text.replace(/<br>/gm, "\n");
    
    if (text.length < 250) {
        console.log(reviewId + ' dropped because too short');
        continue;
    }
   
    var author = review.getElementsByClassName('author');
    if (author.length > 0) {
        author = author[0].innerHTML;   
    } else {
        author = '';
    }
    var reviewDate = review.getElementsByClassName('review-date')[0].innerHTML;
    
    var noteElement = review.getElementsByClassName('a-icon-star')[0];
    var classes = noteElement.className.split(" ");
    var note = '?';

    for (var j = 0; j < classes.length; j++) {
        if (classes[j].startsWith('a-star-')) {
            note = classes[j].substring('7');
        }
    }
    
    var votesElement = review.getElementsByClassName('review-votes');
    if (votesElement.length === 0) {
        continue;   
    }
      
    var votes = votesElement[0].innerHTML;
    votes = votes.substring(0, votes.indexOf(' ')); // Remove the text after the number
    if (votes === 'One') votes = 1;
      
    if (votes < 10 && votes < (totalReviews / 5)) {
        console.log(reviewId + ' dropped because not enough votes');
        continue;
    }  
 
    var reviewTxt = "";
    reviewTxt +=  "# " + headline + "\n\n";
    reviewTxt += "<!--\n";
    reviewTxt += "  -- id: " + reviewId + "\n";
    reviewTxt += "  -- author: " + author + "\n";
    reviewTxt += "  -- date: " + reviewDate + "\n";
    reviewTxt += "  -- book: " + productTitle + "\n";
    reviewTxt += "  -- note: " + note + "\n";
    reviewTxt += "  -- votes: " + votes + "\n";
    reviewTxt += "  -- position: " + (i + 1) + "\n";
    reviewTxt += "  -- total: " + totalReviews + "\n";
    reviewTxt += "  -->\n\n";
    
    reviewTxt += text + "\n\n";
    
    allReviewTxt += reviewTxt;
    count_result++;
}
} catch (e) {
    console.log(e);
}

if (count_result > 0) {
    console.log(count_result, 'send');
    GM_xmlhttpRequest({
      method: "POST",
      url: "http://localhost:8080/api/import",
      data: allReviewTxt,
      headers: {
        "Content-Type": "text/plain"
      },
      onload: function(response) {

        var body = document.getElementsByTagName('body')[0];
        var info = document.createElement('div');
        info.innerHTML = response.responseText;
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
    });

} else {
    var body = document.getElementsByTagName('body')[0];
    var info = document.createElement('div');
    info.innerHTML = 'No review to send';
    info.style.position = 'fixed';
    info.style.bottom = '0';
    info.style.left = '0';
    info.style.right = '0';
    info.style.height = '30px';
    info.style.zIndex = '100';
    info.style.padding = '5px';
    info.style.textAlign = 'center';
    info.style.backgroundColor = 'orange';
    info.style.border = "2px solid black";
    body.appendChild(info);
    
}