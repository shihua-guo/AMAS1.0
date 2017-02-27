(function() {
    'use strict';

    angular
        .module('amasApp')
        .factory('DutySearch', DutySearch);

    DutySearch.$inject = ['$resource'];

    function DutySearch($resource) {
        var resourceUrl =  'api/_search/duties/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
