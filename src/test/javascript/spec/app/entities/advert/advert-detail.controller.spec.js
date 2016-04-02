'use strict';

describe('Controller Tests', function() {

    describe('Advert Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAdvert;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAdvert = jasmine.createSpy('MockAdvert');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Advert': MockAdvert
            };
            createController = function() {
                $injector.get('$controller')("AdvertDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'dniprolabApp:advertUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
