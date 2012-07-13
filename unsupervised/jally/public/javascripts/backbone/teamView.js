/**
 */

window.View = new Object();

// Team View
teamView = Backbone.View.extend({
    initialize: function(){
    	initialize: function(){
            this.render();
        },
        render: function(){
            var template = _.template( $("#search_template").html(), {} );
            this.el.html( template );
        },
        events: {
            "click input[type=button]": "doSearch"
        },
        doSearch: function( event ){
            // Button clicked, you can access the element that was clicked with event.currentTarget
            alert( "Search for " + $("#search_input").val() );
        }
    }
});
var search_view = new SearchView({ el: $("#search_container") });