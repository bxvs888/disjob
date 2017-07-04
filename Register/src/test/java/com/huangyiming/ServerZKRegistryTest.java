package com.huangyiming;

import javax.annotation.Resource;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.junit.Test;

import com.huangyiming.disjob.common.Constants;
import com.huangyiming.disjob.register.center.ServerZKRegistry;

public class ServerZKRegistryTest extends BaseJunitTest{
	 
	 @Resource
	 private ServerZKRegistry serverRegistry;
	
	 @Test
	 public void leaderElectionTest() throws Exception{
		 Builder builder = CuratorFrameworkFactory.builder()
	               .connectString("10.40.6.100:2181,10.40.6.101:2181,10.40.6.102:2181")
	               .retryPolicy(new ExponentialBackoffRetry(1000, 3));
	               //.namespace(Constants.ROOT);
	       builder.sessionTimeoutMs(500);
	       builder.connectionTimeoutMs(300);
	       String latchPath = ZKPaths.makePath(Constants.ROOT, Constants.EJOB_SERVER_NODE_ROOT, Constants.EJOB_SERVER_NODE_MASTER);
	       
	       CuratorFramework client1 = builder.build();
	       CuratorFramework client2 = builder.build();
	       client1.start();
	       client2.start();
	       ZKPaths.mkdirs(client1.getZookeeperClient().getZooKeeper(), latchPath);
	       class LeaderLatchListenerImpl implements LeaderLatchListener{
				@Override
				public void isLeader() {
					System.out.println("Master");
				}

				@Override
				public void notLeader() {
					System.out.println("Slave");
				}
		    	   
		       }
	       
	       LeaderLatch latch1 = new LeaderLatch(client1, latchPath, "1");
	       LeaderLatch latch2 = new LeaderLatch(client2, latchPath, "2");
	       latch1.addListener(new LeaderLatchListenerImpl());
	       latch2.addListener(new LeaderLatchListenerImpl());
	       latch1.start();
	       latch2.start();
	       Thread.sleep(10 * 1000);// 等待Leader选举完成
//	       latch1.await();
//	       latch2.await();
	       
	       LeaderLatch currentLeader = null;
	       if (latch1.hasLeadership())
           {
               currentLeader = latch1;
           }
	       
	       if (latch2.hasLeadership())
           {
               currentLeader = latch2;
           }
	       
	       System.out.println("当前leader：" + currentLeader.getId());
	       
	       latch1.close();
	       latch2.close();
	       client1.close();
	       client2.close();
	 }
	 
	 @Test
	 public void jobServerZKRegistryTest(){
		 System.out.println(serverRegistry.getClient().getState());
	 }
}
