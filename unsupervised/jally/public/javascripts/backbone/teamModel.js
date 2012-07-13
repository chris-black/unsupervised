/**
 */

window.Model = new Object();

// Team Model
Model.Team = Backbone.Model.extend({
    defaults: function() {
        return {
        	name: "",
        	release: "",
        	iterations: {}
        };
    },

    initialize: function() {
        alert("Init Team");
        this.bind("change:name", function(){
            var name = this.get("name"); 
            alert("Changed team name to " + name );
        });
    }
    
    updateName: function( name ){
        this.set({ name: name });
    }
});

// Team Collection
Model.TeamList = Backbone.Collection.extend({

    url: "/backbone/teams",

    // Reference to this collection's model.
    model: Model.Team

});