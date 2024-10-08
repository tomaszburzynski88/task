package contracts

import org.springframework.cloud.contract.spec.Contract

[
        Contract.make {
            description "should return item by id"
            name "should return item by id"

            request {
                url "/item/1"
                method GET()
            }

            response {
                status(200)
                headers {
                    contentType applicationJson()
                }
                body(
                        id: 1,
                        name: "wheel"
                )
            }
        }
]