package com.wriesnig.api.stack;

import org.json.JSONObject;

public class StackApiResponses {


    public static final JSONObject RESPONSE_502 = new JSONObject("""
            {
              "error_id": 502,
              "error_message": "simulated",
              "error_name": "throttle_violation"
            }
            """);

    public static final JSONObject RESPONSE_TAGS = new JSONObject("""
            {
              "items": [
                {
                  "user_id": 2344,
                  "answer_count": 4,
                  "answer_score": 21,
                  "question_count": 2,
                  "question_score": 16,
                  "tag_name": ".net"
                },
                {
                  "user_id": 2344,
                  "answer_count": 5,
                  "answer_score": 17,
                  "question_count": 1,
                  "question_score": 6,
                  "tag_name": "wcf"
                },
                {
                  "user_id": 2344,
                  "answer_count": 8,
                  "answer_score": 14,
                  "question_count": 2,
                  "question_score": 12,
                  "tag_name": "c#"
                }
                ],
                  "has_more": true,
                  "quota_max": 10000,
                  "quota_remaining": 9965
            }""");

    public static final JSONObject RESPONSE_USERS = new JSONObject("""
            {
              "items": [
                {
                  "badge_counts": {
                    "bronze": 9133,
                    "silver": 9046,
                    "gold": 851
                  },
                  "account_id": 11683,
                  "is_employee": false,
                  "last_modified_date": 1676319300,
                  "last_access_date": 1676310494,
                  "reputation_change_year": 8322,
                  "reputation_change_quarter": 8322,
                  "reputation_change_month": 2513,
                  "reputation_change_week": 335,
                  "reputation_change_day": 180,
                  "reputation": 1385502,
                  "creation_date": 1222430705,
                  "user_type": "registered",
                  "user_id": 22656,
                  "accept_rate": 86,
                  "location": "Reading, United Kingdom",
                  "website_url": "http://csharpindepth.com",
                  "link": "https://stackoverflow.com/users/22656/jon-skeet",
                  "profile_image": "https://www.gravatar.com/avatar/6d8ebb117e8d83d74ea95fbdd0f87e13?s=256&d=identicon&r=PG",
                  "display_name": "Jon Skeet"
                },
                {
                  "badge_counts": {
                    "bronze": 0,
                    "silver": 0,
                    "gold": 0
                  },
                  "account_id": 27710156,
                  "is_employee": false,
                  "last_modified_date": 1675620318,
                  "last_access_date": 1676321908,
                  "reputation_change_year": 0,
                  "reputation_change_quarter": 0,
                  "reputation_change_month": 0,
                  "reputation_change_week": 0,
                  "reputation_change_day": 0,
                  "reputation": 1,
                  "creation_date": 1675620277,
                  "user_type": "registered",
                  "user_id": 21153013,
                  "website_url": "https://github.com/johannwriesnig",
                  "link": "https://stackoverflow.com/users/21153013/johannwriesnig",
                  "profile_image": "https://lh3.googleusercontent.com/a/AEdFTp5-samj1gA8JHKWrvcVLEySLoR5yKqEjawBcHs=k-s256",
                  "display_name": "johannwriesnig"
                }
              ],
              "has_more": false,
              "quota_max": 10000,
              "quota_remaining": 9959
            }""");
}
