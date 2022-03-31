package ch.objectifsecurite.thomas.sandbox.springamqpjaeger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class SpringAmqpJaegerApplication {

	private static final boolean NON_DURABLE = false;
	private static final String MY_QUEUE_NAME = "myQueue";

	private final RabbitTemplate template;

	@Autowired
	public SpringAmqpJaegerApplication(RabbitTemplate template) {
		this.template = template;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringAmqpJaegerApplication.class, args);
	}


	@Scheduled(initialDelay = 2000, fixedDelay = 5000)
	public void repeatMe() {
		template.convertAndSend(MY_QUEUE_NAME, String.format("Hello world, it is now %s", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())));
	}

	@Bean
	public Queue myQueue() {
		return new Queue(MY_QUEUE_NAME, NON_DURABLE);
	}

	@RabbitListener(queues = MY_QUEUE_NAME)
	public void listen(String in) {
		System.out.println("Message read from myQueue : " + in);
	}

}
