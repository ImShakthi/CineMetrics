## ⚙️ Current Architecture (Baseline)

**Assumptions:**

- Monolithic or simple microservice setup
- Synchronous API calls to OMDb
- Single PostgreSQL DB
- Spring Boot backend
- Stateless REST APIs with basic auth
- Deployed on a single-node instance or minimal auto-scaling group

This setup is manageable for low traffic (e.g., 100–10k daily users) but will buckle under high load without
enhancement.

----

## 🚀 Scaling for 10M Daily Users — High-Level Changes

### 1. API Gateway & Load Balancing

- Add API Gateway (e.g., NGINX, Spring Cloud Gateway, AWS API Gateway) to:
    - Route requests efficiently
    - Apply rate limiting, throttling
    - Support A/B testing and canary releases
- Use Elastic Load Balancer (ELB) or Kubernetes Ingress Controller for horizontal distribution of traffic.

### 2. Horizontal Scaling with Kubernetes

- Containerize the application using Docker and deploy with Kubernetes (EKS/GKE/AKS).
- Auto-scale pods based on CPU/memory utilization or custom metrics like request count.
- Use HPA (Horizontal Pod Autoscaler) and cluster auto-scaling to handle peak loads.

### 3. Caching Layer

- Introduce Redis or Memcached:
    - Cache frequently accessed movie data (e.g., OMDb responses)
    - Cache top-rated movie list (updated hourly or via background job)
- Use Cache Aside pattern or Spring Cache abstraction for seamless integration.

### 4. Database Optimization & Scaling

- Move from a single-node DB to a read-write split:
    - Primary DB for writes
    - Read replicas (PostgreSQL/Aurora) for read scaling
- Use connection pools (e.g., HikariCP) and tune queries/indexes
- Eventually consider sharding if data grows significantly (e.g., user ratings table)

### 5. Asynchronous Processing with Message Queues

- Decouple synchronous flows:
    - Use Kafka to queue rating submissions
    - Persist ratings asynchronously via worker consumers
- This helps handle bursts and protects the DB from overload.

### 6. External API Resilience (OMDb)

- Rate limit and backoff external API calls using Resilience4j or Spring Retry
- Use circuit breakers to avoid cascading failures
- Optionally, replicate OMDb data into a local cache or datastore if API latency becomes a bottleneck

### 7. Observability & Monitoring

- Integrate:
    - Prometheus + Grafana for metrics
    - ELK stack for centralized logging
    - Jaeger for tracing
- Set up alerts on latency, error rates, queue size, etc.

### 8. CI/CD & Feature Flags

- Use feature flags (e.g. Unleash) to release features gradually

----
📊 How Quality of Service Is Maintained

| Concern           | Solution                                                                 |
|-------------------|--------------------------------------------------------------------------|
| Latency           | Caching, async queues, DB optimization                                   |
| Throughput        | Horizontal scaling (pods, DB replicas, Kafka consumers)                  |
| Fault Tolerance   | Circuit breakers, retries, self-healing infra                            |
| High Availability | Multi-zone deployments, load balancing, health checks                    |
| Data Integrity    | Idempotent operations, deduplication, transactional consistency handling |
| Monitoring        | Logs, metrics, alerts, dashboards                                        |

----
📈 Summary

| Stage          | Daily Users | Architecture Style     | Major Needs                                           |
|----------------|-------------|------------------------|-------------------------------------------------------|
| Early Stage    | 100 – 10k   | Monolith / Simple API  | Basic DB tuning, caching                              |
| Growth Stage   | 10k – 1M    | Microservices / K8s    | Load balancing, async processing                      |
| Scale-Up Stage | 1M – 10M    | Event-driven & Sharded | Queueing, advanced caching, observability, DB scaling |

---
