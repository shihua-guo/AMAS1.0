(function() {
    'use strict';

    angular
        .module('amasApp')
        .factory('DepartmentsOfAssoSearch', DepartmentsOfAssoSearch);

    DepartmentsOfAssoSearch.$inject = ['$resource'];

    function DepartmentsOfAssoSearch($resource) {
        var resourceUrl =  'api/_search/departmentsOfAsso/:id';

        return $resource(resourceUrl, {id:'@assoId'}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
