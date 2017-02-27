(function() {
    'use strict';
    angular
        .module('amasApp')
        .factory('Duty', Duty);

    Duty.$inject = ['$resource'];

    function Duty ($resource) {
        var resourceUrl =  'api/duties/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
