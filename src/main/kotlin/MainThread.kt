import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import java.util.concurrent.TimeUnit

const val lockNm = "LOCK_NM"
const val keyNm = "LOCK_KEY"

class MainThread : Runnable {

    // jdk11 에서 netty thread illegal access 에러 발생. run method 내부로 처리.
    companion object {
        private val config = Config()

        fun getRedissonClient() : RedissonClient {
            config.useSingleServer().setAddress("redis://localhost:6379")
            config.useSingleServer()
            return Redisson.create(config)
        }
    }

    override fun run() {
//        val redissonClient = getRedissonClient()
        val redissonClient = Redisson.create() // default redis://localhost:6379
        for (i in (0 until 10000)) {
            // redis 에 session 이 key 이름으로 보여진다. session 이 종료되면 사라진다.
            val lock = redissonClient.getLock(lockNm)
            // waitTime 이 지나는 동안 lock 이 획득되지 않으면 실패한다.
            // leaseTime 이 지나면 lock 획득이 성공된 이후 자동으로 lock 이 해제된다.
            if (lock.tryLock(1000, 1000, TimeUnit.MILLISECONDS)) {
                try {
                    val value = redissonClient.getAtomicLong(keyNm)
                    println(value.incrementAndGet())
                } finally {
                    lock.unlock()
                }
            }
        }
        println("result : ${redissonClient.getAtomicLong(keyNm).get()}")
    }
}