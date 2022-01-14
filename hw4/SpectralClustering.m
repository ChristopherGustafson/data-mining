tic

input = "data/example1.dat";

% Read input file
E = csvread(input);

% Extract the two columns represesnting edge source and target
col1 = E(:,1);
col2 = E(:,2);
max_id = max(max(col1,col2));

% STEP 1: Construct graph and remove edge duplicates and self loops
G = graph(col1, col2);
graphPlot = plot(G);
A = adjacency(G);

% STEP 2: Define degree matrix D, and then thsymmetric normalized Laplacian matrix L
D = diag(sum(A, 2));
L = D^(-1/2) * A * D^(-1/2);

% Find optimal k by finding max eigengap
[vects, vals] = eigs(L);
diffs = flip(diff(diag(vals)));
[maxDiff, k] = max(diffs);

% STEP 3: Find eigenvectors for the k largest eigen values of L
[X, D] = eigs(L, k);

% STEP 4: Renormalize
Y = normr(X);

% STEP 5: Find k clusters using k-means, first find optimal k
K = kmeans(Y, k);

% STEP 6: Assign initial nodes to clusters according to kmeans results
for i = 1:1:max_id 
    switch K(i)
        case 1
            highlight(graphPlot, i, 'NodeColor', [1 0 0]);
        case 2
            highlight(graphPlot, i, 'NodeColor', [0 1 0]);
        case 3
            highlight(graphPlot, i, 'NodeColor', [0 0 1]);
        otherwise
            highlight(graphPlot, i, 'NodeColor', [1 1 0]);
    end
end

toc