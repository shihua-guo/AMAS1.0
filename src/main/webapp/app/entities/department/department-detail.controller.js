(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('DepartmentDetailController', DepartmentDetailController);

    DepartmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Department', 'Duty', 'Association', 'Amember'];

    function DepartmentDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Department, Duty, Association, Amember) {
        var vm = this;

        vm.department = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('amasApp:departmentUpdate', function(event, result) {
            vm.department = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
