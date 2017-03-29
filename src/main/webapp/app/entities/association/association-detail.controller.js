(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('AssociationDetailController', AssociationDetailController);

    AssociationDetailController.$inject = ['assoService','$http','$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Association', 'Department', 'Activity', 'Property', 'Amember','$uibModalInstance'];

    function AssociationDetailController(assoService,$http,$scope, $rootScope, $stateParams, previousState, DataUtils, entity, Association, Department, Activity, Property, Amember,$uibModalInstance) {
        var vm = this;

        vm.association = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.amemberNum = '';
        vm.assoDeptNames = [];
        vm.clear = clear;
        //获取该社团的成员数量
       $http.get('/api/getAssoAmemberNum/'+entity.id).then(function (response) {
                vm.amemberNum = response.data;
        });
        //获取该社团的部门名称
       $http.get('/api/getAssoDeptNameByAssoId/'+entity.id).then(function (response) {
                vm.assoDeptNames = response.data;
        });
        //获取该社团的近期活动
       $http.get('/api/getRecentActivitiesByAssoId/'+entity.id).then(function (response) {
                vm.activitie = response.data;
        });
       //设置社团的id和name，在各个controller种共享
       assoService.setAssoId(entity.id);
       
       //alert("$scope.queryAmemNum"+$scope.queryAmemNum);
        var unsubscribe = $rootScope.$on('amasApp:associationUpdate', function(event, result) {
            vm.association = result;
        });

        function clear () { 
            $uibModalInstance.dismiss('cancel');
        }
        $scope.$on('$destroy', unsubscribe);
    }
})();
