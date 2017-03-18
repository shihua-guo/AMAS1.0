(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('AssociationDetailController', AssociationDetailController);

    AssociationDetailController.$inject = ['assoAmemNumService','$http','$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Association', 'Department', 'Activity', 'Property', 'Amember'];

    function AssociationDetailController(assoAmemNumService,$http,$scope, $rootScope, $stateParams, previousState, DataUtils, entity, Association, Department, Activity, Property, Amember) {
        var vm = this;

        vm.association = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        //获取该社团的成员数量
       $http.get('/api/getAssoAmemberNum/'+entity.id).then(function (response) {
                vm.amemberNum = response.data;
        });
       assoAmemNumService.setAssoId(entity.id);
       //alert("$scope.queryAmemNum"+$scope.queryAmemNum);
        var unsubscribe = $rootScope.$on('amasApp:associationUpdate', function(event, result) {
            vm.association = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
