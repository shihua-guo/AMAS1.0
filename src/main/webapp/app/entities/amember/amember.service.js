(function() {
    'use strict';
    angular
        .module('amasApp')
        .factory('Amember', Amember);

    Amember.$inject = ['$resource', 'DateUtils'];

    function Amember ($resource, DateUtils) {
        var resourceUrl =  'api/amembers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.membJoinDate = DateUtils.convertLocalDateFromServer(data.membJoinDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.membJoinDate = DateUtils.convertLocalDateToServer(copy.membJoinDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.membJoinDate = DateUtils.convertLocalDateToServer(copy.membJoinDate);
                    return angular.toJson(copy);
                }
            },
            'getAssoIdAndName': { method: 'GET', isArray: true, url: 'api/getAssoIdAndName', 
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;                
                }
            }

        });
    }
})();
