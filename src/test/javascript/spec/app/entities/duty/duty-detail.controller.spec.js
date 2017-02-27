'use strict';

describe('Controller Tests', function() {

    describe('Duty Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDuty, MockDepartment, MockAmember;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDuty = jasmine.createSpy('MockDuty');
            MockDepartment = jasmine.createSpy('MockDepartment');
            MockAmember = jasmine.createSpy('MockAmember');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Duty': MockDuty,
                'Department': MockDepartment,
                'Amember': MockAmember
            };
            createController = function() {
                $injector.get('$controller')("DutyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'amasApp:dutyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
