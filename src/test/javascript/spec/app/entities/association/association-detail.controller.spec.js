'use strict';

describe('Controller Tests', function() {

    describe('Association Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAssociation, MockDepartment, MockActivity, MockProperty, MockAmember, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAssociation = jasmine.createSpy('MockAssociation');
            MockDepartment = jasmine.createSpy('MockDepartment');
            MockActivity = jasmine.createSpy('MockActivity');
            MockProperty = jasmine.createSpy('MockProperty');
            MockAmember = jasmine.createSpy('MockAmember');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Association': MockAssociation,
                'Department': MockDepartment,
                'Activity': MockActivity,
                'Property': MockProperty,
                'Amember': MockAmember,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("AssociationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'amasApp:associationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
