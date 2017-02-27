(function() {
    'use strict';

    angular
        .module('amasApp')
        .factory('ActivitySearch', ActivitySearch);

    ActivitySearch.$inject = ['$resource'];

    function ActivitySearch($resource) {
        var resourceUrl =  'api/_search/activities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
