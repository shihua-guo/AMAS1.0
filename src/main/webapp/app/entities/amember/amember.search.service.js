(function() {
    'use strict';

    angular
        .module('amasApp')
        .factory('AmemberSearch', AmemberSearch);

    AmemberSearch.$inject = ['$resource'];

    function AmemberSearch($resource) {
        var resourceUrl =  'api/_search/amembers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
