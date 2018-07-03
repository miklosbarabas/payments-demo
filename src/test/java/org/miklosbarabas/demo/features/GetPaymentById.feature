Feature: Manage Payment resources

  Scenario: User calls web service to get a Payment resource by its ID
    Given a Payment resource exists with an ID of 4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    When a user retrieves the Payment resource by ID
    Then the status code is 200
    And response includes the following
#    And response includes the following in any order
      | type            | Payment                              |
      | organisation_id | 743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb |

