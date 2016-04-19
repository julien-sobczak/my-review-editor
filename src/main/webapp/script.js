var lastQuery = "";

// Prevent the user from leaving without saving his review before
window.onbeforeunload = function(e) {
	return 'Modifications will not be saved.';
};

document.addEventListener("DOMContentLoaded", function(event) {
    console.log("DOM fully loaded and parsed");

    var examples = document.getElementById('examples');

    /**
     * Return if the character is a phrase separating character.
     *
     * @param c the character to test
     * @returns {boolean} true if the character should be considered as a phrase separating character
     */
    function isSeparationCharacter(c) {
        var separationCharacter = [ '.', ';', '!', '?' ];
        return separationCharacter.indexOf(c) != -1;
    }

    // Implementation:
    // To list each key entering on the textarea:
    // document.getElementById("input").onkeyup = function() {}

    /**
     * Scheduled function to search examples regularly.
     */
    var fillExamples = function() {

        /**
         * Extract the Lucene Query from the review text.
         */
        var getQuery = function() {
            var i;
            var input = document.getElementById("input");
            var review = input.value;
            var cursor = input.selectionStart;
            var length = review.length;
            // "phrase1|.phrase2" => review.charAt(cursor) == '.'
            //         |-> cursor
            // We want to consider phrase1 in this case:
            if (cursor > 0 && isSeparationCharacter(review.charAt(cursor))) {
                cursor--;
            }

            var start = 0;
            var end = review.length;

            for (i = cursor; i >= 0; i--) {
                if (isSeparationCharacter(review.charAt(i))) {
                    start = i + 1;
                    break;
                }
            }

            for (i = cursor; i < end; i++) {
                if (isSeparationCharacter(review.charAt(i))) {
                    end = i;
                    break;
                }
            }

            var query = review.substring(start, end).trim();
            return query;
        };

        /**
         * Process the received examples.
         *
         * @param json the raw JSON document containing the results to display
         */
        var processExamples = function(json) {
            var phrase = json.query;
            var resultsCount = json.total;

            var d = document.createDocumentFragment();

            /*
            <div>
              <span id="results-count">10</span> results for "<span id="phrase"></span>"
            </div>
            */
            var divMessage = document.createElement('div');
            divMessage.id = 'global-message';
            divMessage.innerHTML = '<span id="results-count">' + resultsCount + '</span> results for "<span>' + phrase + '</span>"';
            d.appendChild(divMessage);

            /*
             <div class="example">
               <div class="text">
                 This book was <em>fascinating</em>. A really well-writen book to read from <em>cover to cover</em>.
               </div>
               <div class="source"><i class="fa fa-heart"></i> 10 <i class="fa fa-info-circle source-link"></i></div>
             </div>
             */

            for (var i = 0; i < json.hits.length; i++) {
                var hit = json.hits[i];

                var divExample = document.createElement('div');
                divExample.classList.add('example');


                var divText = document.createElement('div');
                divText.classList.add('text');
                divText.innerHTML = hit.extractHighlight;


                var divMore = document.createElement('div');
                divMore.classList.add('more');

                var p1More = document.createElement('p');
                var pMoreText = '';
                pMoreText += '<span class="book">' + hit.book + '</span>';
                pMoreText += ' by <span class="reviewer">' + hit.reviewer + '</span>';
                pMoreText += ', <span class="date">' + hit.date + '</span>';
                p1More.innerHTML = pMoreText;

                var p2More = document.createElement('p');
                p2More.innerHTML = hit.titleHighlight;

                var p3More = document.createElement('p');
                p3More.innerHTML = hit.commentHighlight;

                divMore.appendChild(p1More);
                divMore.appendChild(p2More);
                divMore.appendChild(p3More);


                var divSource = document.createElement('div');
                divSource.classList.add('source');
                divSource.innerHTML = '';
                for (var j = 0; j < hit.note; j++) {
                	divSource.innerHTML += '<i class="fa fa-star nospace"></i>';
                }
                for (var k = hit.note; k < 5; k++) {
                	divSource.innerHTML += '<i class="fa fa-star-o nospace"></i>';
                }
                divSource.innerHTML +=
                	'<i class="fa fa-calendar-o"></i>' + hit.date +
                    '<i class="fa fa-heart"></i>' + hit.votes +
                    '<i class="fa fa-navicon"></i>' + hit.rank +
                    '<i class="fa fa-expand example-expand"></i>';


                divExample.appendChild(divText);
                divExample.appendChild(divMore);
                divExample.appendChild(divSource);

                d.appendChild(divExample);
            }

            while (examples.firstChild) {
                examples.removeChild(examples.firstChild);
            }
            examples.appendChild(d);

            var links = document.getElementsByClassName('example-expand');
            Array.prototype.forEach.call(links, function(link) {
               link.onclick = function() {
                   if (this.classList.contains('fa-expand')) { // Should expand
                       var exampleNode = this.parentNode.parentNode;
                       exampleNode.children[0].style.display = 'none'; // div.text
                       exampleNode.children[1].style.display = 'block'; // div.more
                       this.classList.remove('fa-expand');
                       this.classList.add('fa-compress');
                   } else { // Should compress
                       var exampleNode = this.parentNode.parentNode;
                       exampleNode.children[0].style.display = 'block'; // div.text
                       exampleNode.children[1].style.display = 'none'; // div.more
                       this.classList.remove('fa-compress');
                       this.classList.add('fa-expand');
                   }
               }
            });


        };

        /**
         * Launch the query.
         *
         * @param query the raw query string extracted from the review text.
         */
        var search = function(query) {
    		// Avoid useless call
        	if (query == lastQuery) {
        		return;
        	}
        	
        	// Avoid to search for only a few characters
        	if (query === "" || query.length <= 3) {
        		return;
        	}
        	
        	lastQuery = query;
        	
            window.fetch('http://localhost:8080/api/search?q=' + encodeURIComponent(query)).
                then(function(response) {
                    return response.json();
                }).then(function(json) {
                	processExamples(json);
                }).catch(function(ex) {
                    console.log('parsing failed', ex)
                });
        };

        // Run
        search(getQuery());
    };

    //fillExamples();
    var intervalID = window.setInterval(fillExamples, 1000);


});