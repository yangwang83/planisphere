Planisphere is a project platform to build and visualize distributed protocols (e.g. 2PC, Paxos, BFT, etc). It targets two goals: first, it can help the instructor demonstrate the flow of a protocol in an interactive manner; second, it simplifies the job of building a distributed protocol, so that a student can build a complicated protocol within a reasonable amount of time. To be concrete,

1. It provides a network implementation, so that the user can focus on the logic of the distributed protocol.

2. It allows the user to drop and re-order messages and/or kill nodes during one test.

3. It visualizes the whole execution in a web page and allows the user to customize the drop/re-order/kill in the web page
