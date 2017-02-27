'use strict';

describe('Controller Tests', function() {

    describe('Amember Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAmember, MockAssociation, MockDepartment, MockRole, MockDuty;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAmember = jasmine.createSpy('MockAmember');
            MockAssociation = jasmine.createSpy('MockAssociation');
            MockDepartment = jasmine.createSpy('MockDepartment');
            MockRole = jasmine.createSpy('MockRole');
            MockDuty = jasmine.createSpy('MockDuty');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Amember': MockAmember,
                'Association': MockAssociation,
                'Department': MockDepartment,
                'Role': MockRole,
                'Duty': MockDuty
            };
            createController = function() {
                $injector.get('$controller')("AmemberDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'amasApp:amemberUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
