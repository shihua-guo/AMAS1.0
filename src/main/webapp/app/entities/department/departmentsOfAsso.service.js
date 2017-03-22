(function() {
    'use strict';
    angular
        .module('amasApp')
        .factory('DepartmentsOfAsso', DepartmentsOfAsso);

    DepartmentsOfAsso.$inject = ['$resource'];

    function DepartmentsOfAsso ($resource) {
        var resourceUrl =  'api/departmentsOfAsso/:id';

        return $resource(resourceUrl, {id:'@assoId'}, {
            'queryDeptsOfAsso': { method: 'GET', isArray: true}
        });
    }
})();
