(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('AssociationDetailController', AssociationDetailController);

    AssociationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Association', 'Department', 'Activity', 'Property', 'Amember'];

    function AssociationDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Association, Department, Activity, Property, Amember) {
        var vm = this;

        vm.association = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('amasApp:associationUpdate', function(event, result) {
            vm.association = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
