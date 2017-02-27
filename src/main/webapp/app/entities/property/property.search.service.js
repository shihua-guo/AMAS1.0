(function() {
    'use strict';

    angular
        .module('amasApp')
        .factory('PropertySearch', PropertySearch);

    PropertySearch.$inject = ['$resource'];

    function PropertySearch($resource) {
        var resourceUrl =  'api/_search/properties/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
