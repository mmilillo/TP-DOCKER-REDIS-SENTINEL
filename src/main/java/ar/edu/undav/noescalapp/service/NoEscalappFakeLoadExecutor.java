package ar.edu.undav.noescalapp.service;

import com.martensigwart.fakeload.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The default {@link FakeLoadExecutor} implementation.
 *
 * <p>
 * The {@code DefaultFakeLoadExecutor} executes a submitted {@code FakeLoad} by passing it
 * to the {@link FakeLoadScheduler#schedule(FakeLoad)} method which schedules each {@code FakeLoad}
 * for execution. Method {@code schedule(FakeLoad)} returns a {@link Future} which is done when
 * the execution of the submitted {@code FakeLoad} object has completed. By calling {@link Future#get()}
 * the {@code execute(FakeLoad)} method blocks until either execution has finished or an exception was thrown.
 * This blocking behavior conforms to the general contract as imposed by {@link FakeLoadExecutor#execute(FakeLoad)}.
 *
 * <p>
 * Multiple {@code FakeLoad} objects being executed simultaneously produce a system load which is the aggregation of all
 * load instructions contained in the objects. If thread A submits a {@code FakeLoad} of 20% CPU and thread B submits
 * a {@code FakeLoad} of 30% CPU the resulting system load should be ~ 50%.
 *
 *
 * @since 1.8
 * @see FakeLoadExecutor
 * @see FakeLoadScheduler
 * @see FakeLoad
 *
 * @author Marten Sigwart
 */
public class NoEscalappFakeLoadExecutor implements FakeLoadExecutor {

    private static final Logger log = LoggerFactory.getLogger(NoEscalappFakeLoadExecutor.class);
    private static final String DISK_INPUT_FILE = "input.tmp";
    private static final String DISK_OUTPUT_FILE = "output.tmp";
    private static final String DEFAULT_DISK_INPUT_PATH = System.getProperty("java.io.tmpdir") + "/" + DISK_INPUT_FILE;
    private static final String DEFAULT_DISK_OUTPUT_PATH = System.getProperty("java.io.tmpdir") + "/" + DISK_OUTPUT_FILE;
    private static SimulationInfrastructure defaultInfrastructure;

    /**
     * The scheduler used for scheduling {@link FakeLoad} objects for execution.
     */
    private final FakeLoadScheduler scheduler;

    /**
     * Creates a new {@code DefaultFakeLoadExecutor} instance.
     * @param scheduler the scheduler to be used by the newly created executor
     */
    public NoEscalappFakeLoadExecutor(FakeLoadScheduler scheduler) {
        this.scheduler = scheduler;
    }


    @Override
    public void execute(FakeLoad load) {
        try {
            log.info("Starting FakeLoad execution...");
            log.debug("Executing {}", load);
            Future<Void> future = scheduler.schedule(load);
            future.get();
            log.info("Finished FakeLoad execution.");
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Problema! Devolvemos 5xx");
            throw new IllegalStateException("Problema de carga");
        }
    }


    public static synchronized FakeLoadExecutor newNoEscalappFakeExecutor() {
        // create infrastructure if it hasn't been created yet
        if (defaultInfrastructure == null) {

            try {
                int noOfCores = Runtime.getRuntime().availableProcessors();

                // Create DiskInput Simulator
                DiskInputSimulator diskInputSimulator;
                diskInputSimulator = new RandomAccessDiskInputSimulator(DEFAULT_DISK_INPUT_PATH);

                // Create DiskOutput Simulator
                DiskOutputSimulator diskOutputSimulator;
                diskOutputSimulator = new RandomAccessDiskOutputSimulator(DEFAULT_DISK_OUTPUT_PATH);

                // Create Memory Simulator
                MemorySimulator memorySimulator = new MemorySimulator();

                // Create CPU Simulators
                List<CpuSimulator> cpuSimulators = new ArrayList<>();
                for (int i = 0; i < noOfCores; i++) {
                    cpuSimulators.add(new FibonacciCpuSimulator());
                }

                // Inject dependencies for LoadController
                LoadController controller;
                controller = new LoadController(new SystemLoad(),
                        cpuSimulators, memorySimulator, diskInputSimulator, diskOutputSimulator);


                // Create thread pool
                ExecutorService executorService = Executors.newFixedThreadPool(
                        noOfCores + 4);


                defaultInfrastructure = new DefaultSimulationInfrastructure(executorService, controller);


                /*
                 * Catch blocks in case paths can be passed as parameters.
                 */
            } catch (IOException e) {
                log.error("File {} used for simulating disk output could not be created.", DEFAULT_DISK_OUTPUT_PATH);
                throw new IllegalArgumentException(e);
            }
        }

        return new NoEscalappFakeLoadExecutor(new DefaultFakeLoadScheduler(defaultInfrastructure));
    }

}
