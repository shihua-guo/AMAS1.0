(function() {
    'use strict';
    angular
        .module('amasApp')
        .factory('Property', Property);

    Property.$inject = ['$resource', 'DateUtils'];

    function Property ($resource, DateUtils) {
        var resourceUrl =  'api/properties/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.propBuyDate = DateUtils.convertLocalDateFromServer(data.propBuyDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.propBuyDate = DateUtils.convertLocalDateToServer(copy.propBuyDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.propBuyDate = DateUtils.convertLocalDateToServer(copy.propBuyDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
