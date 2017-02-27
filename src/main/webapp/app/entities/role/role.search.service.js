(function() {
    'use strict';

    angular
        .module('amasApp')
        .factory('RoleSearch', RoleSearch);

    RoleSearch.$inject = ['$resource'];

    function RoleSearch($resource) {
        var resourceUrl =  'api/_search/roles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
