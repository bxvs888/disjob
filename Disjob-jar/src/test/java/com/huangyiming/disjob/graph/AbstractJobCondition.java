package com.huangyiming.disjob.graph;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.huangyiming.disjob.event.BaseCondition;
import com.huangyiming.disjob.java.ExecutorBuilder;
import com.huangyiming.disjob.java.job.DependEJob;

public abstract class AbstractJobCondition extends BaseCondition<Node<DependEJob>, Set<Node<DependEJob>>> {

	protected Scheduler scheduler ;
	protected ConcurrentHashMap<Node<DependEJob>,AtomicInteger> messageCountMap = null;
	protected int messageTotal = 0;
	
	public AbstractJobCondition(Scheduler schedule,Node<DependEJob> observiable,Set<Node<DependEJob>> v) {
		super(observiable, v);
		this.scheduler = schedule ;
		if(v!=null){
			this.messageCountMap = new ConcurrentHashMap<Node<DependEJob>, AtomicInteger>();
			this.messageTotal = v.size();
			Iterator<Node<DependEJob>> iter = getValue().iterator();
			while(iter.hasNext()){
				Node<DependEJob> tmp = iter.next();
				messageCountMap.put(tmp, new AtomicInteger(0));
			}
		}
	}

	@Override
	public abstract boolean isFinished() ;

	public abstract void increMessageCount(Node<DependEJob> targetJobNode);
	
	@Override
	public void handler() {
		if(this.isFinished()){
			//一次消息消费成功，对每一个前驱节点的消息数减一。
			for(AtomicInteger value : messageCountMap.values()){
				if(value.get()>=1){
					value.decrementAndGet();
				}
			}
			
			ExecutorBuilder.getExecutor().execute(new JobAction(observiable, scheduler));
		}else{
			System.out.println(getObserviable().getVal().getKey()+"还没有达到条件:");
		}
	}
}
