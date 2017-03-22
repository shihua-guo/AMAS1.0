(function() {
    'use strict';

    angular
        .module('amasApp')
        .factory('ActivitysOfAssoSearch', ActivitysOfAssoSearch);

    ActivitysOfAssoSearch.$inject = ['$resource'];

    function ActivitysOfAssoSearch($resource) {
        var resourceUrl =  'api/_search/activitiesOfAsso/:id';

        return $resource(resourceUrl, {id:'@assoId'}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
