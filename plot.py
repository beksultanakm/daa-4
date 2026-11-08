import csv
import matplotlib.pyplot as plt

rows = list(csv.DictReader(open("results/results.csv")))

def series(experiment, algorithm, case):
    sizes, times = [], []
    for r in rows:
        if r["experiment"] == experiment and r["algorithm"] == algorithm and r["case"] == case:
            sizes.append(int(r["size"]))
            times.append(float(r["avg_time_ms"]))
    return sizes, times

plt.figure()
for alg in ["LCS", "EditDistance"]:
    x, y = series("DP", alg, "random")
    plt.plot(x, y, marker="o", label=alg)
plt.xlabel("String length")
plt.ylabel("Average time (ms)")
plt.title("LCS and Edit Distance")
plt.legend()
plt.grid(True)
plt.savefig("plots/dp_time.png")

plt.figure()
for alg in ["Naive", "KMP", "RabinKarp"]:
    for case, style in [("present", "-"), ("absent", "--")]:
        x, y = series("Matching", alg, case)
        plt.plot(x, y, style, marker="o", label=alg + " (" + case + ")")
plt.xlabel("Text length")
plt.ylabel("Average time (ms)")
plt.title("String matching")
plt.legend()
plt.grid(True)
plt.savefig("plots/matching_time.png")

plt.figure()
x, y = series("Kasai", "Kasai", "random")
plt.plot(x, y, marker="o", label="Kasai")
plt.xlabel("String length")
plt.ylabel("Average time (ms)")
plt.title("Kasai LCP")
plt.legend()
plt.grid(True)
plt.savefig("plots/kasai_time.png")

print("Plots saved to plots/")