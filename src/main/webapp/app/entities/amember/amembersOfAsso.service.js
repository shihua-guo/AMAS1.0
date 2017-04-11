(function() {
    'use strict';
    angular
        .module('amasApp')
        .factory('AmembersOfAsso', AmembersOfAsso);

    AmembersOfAsso.$inject = ['$resource', 'DateUtils'];

    function AmembersOfAsso ($resource, DateUtils) {
        var resourceUrl =  'api/amembersOfAsso/:id';

        return $resource(resourceUrl, {}, {
            'queryAmembersOfAsso': { method: 'GET', isArray: true}
        })
    }
})();
