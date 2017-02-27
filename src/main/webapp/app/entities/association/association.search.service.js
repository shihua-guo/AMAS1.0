(function() {
    'use strict';

    angular
        .module('amasApp')
        .factory('AssociationSearch', AssociationSearch);

    AssociationSearch.$inject = ['$resource'];

    function AssociationSearch($resource) {
        var resourceUrl =  'api/_search/associations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
