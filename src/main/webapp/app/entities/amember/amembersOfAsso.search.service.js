(function() {
    'use strict';

    angular
        .module('amasApp')
        .factory('AmembersOfAssoSearch', AmembersOfAssoSearch);

    AmembersOfAssoSearch.$inject = ['$resource'];

    function AmembersOfAssoSearch($resource) {
        var resourceUrl =  'api/_search/amembersOfAsso/:id';

        return $resource(resourceUrl, {id:'@assoId'}, {
            'query': { method: 'GET', isArray: true }
        });
    }
})();
