(function() {
    'use strict';
    angular
        .module('amasApp')
        .factory('Association', Association);

    Association.$inject = ['$resource', 'DateUtils'];
    //传递associationId
    angular
        .module('amasApp')
        .service('assoService', function() {
          //获取社团的id
          var assoId ='' ;
          var setAssoId = function(num) {
              assoId=num;
          };
          var getAssoId = function(){
              return assoId;
          };

          //获取社团的名称
          var assoName ='' ;
          var setAssoName = function(name) {
              assoName=name;
          };

          var getAssoName = function(){
              return assoName;
          };

          return {
            setAssoId: setAssoId,
            getAssoId: getAssoId,
            setAssoName: setAssoName,
            getAssoName: getAssoName
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
