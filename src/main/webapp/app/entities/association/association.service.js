(function() {
    'use strict';
    angular
        .module('amasApp')
        .factory('Association', Association);

    Association.$inject = ['$resource', 'DateUtils'];
    //传递associationId
    angular
        .module('amasApp')
        .service('assoAmemNumService', function() {
          var assoId ='' ;

          var setAssoId = function(num) {
              assoId=num;
          };

          var getAssoId = function(){
              return assoId;
          };

          return {
            setAssoId: setAssoId,
            getAssoId: getAssoId
          };

        });


    function Association ($resource, DateUtils) {
        var resourceUrl =  'api/associations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.assoFoundDate = DateUtils.convertLocalDateFromServer(data.assoFoundDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.assoFoundDate = DateUtils.convertLocalDateToServer(copy.assoFoundDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.assoFoundDate = DateUtils.convertLocalDateToServer(copy.assoFoundDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
