'use strict';

describe('Controller Tests', function() {

    describe('Department Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDepartment, MockDuty, MockAssociation, MockAmember;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDepartment = jasmine.createSpy('MockDepartment');
            MockDuty = jasmine.createSpy('MockDuty');
            MockAssociation = jasmine.createSpy('MockAssociation');
            MockAmember = jasmine.createSpy('MockAmember');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Department': MockDepartment,
                'Duty': MockDuty,
                'Association': MockAssociation,
                'Amember': MockAmember
            };
            createController = function() {
                $injector.get('$controller')("DepartmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'amasApp:departmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
